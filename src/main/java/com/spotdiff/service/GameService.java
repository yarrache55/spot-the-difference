package com.spotdiff.service;

import com.spotdiff.model.*;
import com.spotdiff.repository.GameSaveRepository;
import com.spotdiff.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class GameService {

    @Autowired
    private LevelService levelService;
    @Autowired
    private GameSaveRepository gameSaveRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PlayerService playerService;

    /**
     * Process a click on the modified image.
     * Returns a result map with: hit, diffId, alreadyFound, levelComplete, score.
     */
    public Map<String, Object> processClick(GameSession session, int levelNumber,
            double xPercent, double yPercent) {
        Map<String, Object> result = new HashMap<>();
        Optional<Level> levelOpt = levelService.getLevelByNumber(levelNumber);

        if (levelOpt.isEmpty()) {
            result.put("error", "Niveau introuvable");
            return result;
        }

        Level level = levelOpt.get();
        boolean hit = false;

        for (Difference diff : level.getDifferences()) {
            if (diff.isHit(xPercent, yPercent)) {
                hit = true;
                boolean alreadyFound = session.hasDifferenceBeenFound(diff.getId());
                result.put("alreadyFound", alreadyFound);
                result.put("diffId", diff.getId());
                result.put("xPercent", diff.getXPercent());
                result.put("yPercent", diff.getYPercent());

                if (!alreadyFound) {
                    session.addFoundDifference(diff.getId(), level.getPointsPerDiff());
                }
                break;
            }
        }

        if (!hit) {
            session.registerWrongClick();
            result.put("wrongClick", true);
        }

        result.put("hit", hit);
        result.put("score", session.getScore());
        result.put("differencesFound", session.getDifferencesFound());
        result.put("totalDifferences", level.getTotalDifferences());

        boolean levelComplete = session.getDifferencesFound() >= level.getTotalDifferences();
        result.put("levelComplete", levelComplete);
        result.put("isLastLevel", levelService.isLastLevel(levelNumber));

        return result;
    }

    /**
     * Save the current game state to DB.
     */
    @Transactional
    public void saveGame(GameSession session, String username) {
        playerRepository.findByUsername(username).ifPresent(player -> {
            GameSave save = new GameSave(
                    player,
                    session.getCurrentLevel(),
                    session.getScore(),
                    session.getDifferencesFound(),
                    session.getElapsedSeconds());
            gameSaveRepository.save(save);
            playerService.updateScore(username, session.getScore(), session.getCurrentLevel());
        });
    }

    /**
     * Load the latest save for a player.
     */
    public Optional<GameSave> loadLatestSave(String username) {
        return playerRepository.findByUsername(username)
                .flatMap(gameSaveRepository::findTopByPlayerOrderBySavedAtDesc);
    }

    /**
     * Get a hint for the next unfound difference.
     */
    public Map<String, Object> getHint(GameSession session, int levelNumber) {
        Map<String, Object> result = new HashMap<>();
        Optional<Level> levelOpt = levelService.getLevelByNumber(levelNumber);

        if (levelOpt.isEmpty()) {
            result.put("error", "Niveau introuvable");
            return result;
        }

        Level level = levelOpt.get();

        for (Difference diff : level.getDifferences()) {
            if (!session.hasDifferenceBeenFound(diff.getId())) {
                // Return the center point in percent coordinates.
                result.put("hintX", diff.getXPercent());
                result.put("hintY", diff.getYPercent());
                result.put("diffId", diff.getId());
                return result;
            }
        }

        result.put("error", "Toutes les différences ont été trouvées");
        return result;
    }
}
