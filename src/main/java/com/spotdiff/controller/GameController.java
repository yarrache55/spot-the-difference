package com.spotdiff.controller;

import com.spotdiff.model.GameSession;
import com.spotdiff.model.Level;
import com.spotdiff.service.GameService;
import com.spotdiff.service.LevelService;
import com.spotdiff.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;

@Controller
public class GameController {

    @Autowired
    private LevelService levelService;
    @Autowired
    private GameService gameService;
    @Autowired
    private PlayerService playerService;

    /**
     * Start or resume a level.
     */
    @GetMapping("/game/{levelNumber}")
    public String gamePage(@PathVariable int levelNumber,
            @AuthenticationPrincipal UserDetails userDetails,
            HttpSession session, Model model) {

        Optional<Level> levelOpt = levelService.getLevelByNumber(levelNumber);
        if (levelOpt.isEmpty())
            return "redirect:/menu";

        Level level = levelOpt.get();

        // Init or reset session for this level
        GameSession gameSession = (GameSession) session.getAttribute("gameSession");
        if (gameSession == null || gameSession.getCurrentLevel() != levelNumber) {
            gameSession = new GameSession();
            gameSession.reset(levelNumber);
            session.setAttribute("gameSession", gameSession);
        }

        playerService.findByUsername(userDetails.getUsername()).ifPresent(p -> model.addAttribute("player", p));

        model.addAttribute("level", level);
        model.addAttribute("gameSession", gameSession);
        model.addAttribute("totalLevels", levelService.getTotalLevels());
        model.addAttribute("foundIds", gameSession.getFoundDifferenceIds());

        return "game/play";
    }

    /**
     * AJAX endpoint: process a click on the modified image.
     * Body: { xPercent: float, yPercent: float, levelNumber: int }
     */
    @PostMapping("/game/click")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleClick(
            @RequestBody Map<String, Object> body,
            @AuthenticationPrincipal UserDetails userDetails,
            HttpSession session) {

        GameSession gameSession = (GameSession) session.getAttribute("gameSession");
        if (gameSession == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Session expirée"));
        }

        double xPercent = ((Number) body.get("xPercent")).doubleValue();
        double yPercent = ((Number) body.get("yPercent")).doubleValue();
        int levelNumber = ((Number) body.get("levelNumber")).intValue();

        Map<String, Object> result = gameService.processClick(gameSession, levelNumber, xPercent, yPercent);

        // Auto-save on level completion
        if (Boolean.TRUE.equals(result.get("levelComplete"))) {
            gameService.saveGame(gameSession, userDetails.getUsername());
        }

        return ResponseEntity.ok(result);
    }

    /**
     * AJAX endpoint: get a hint for the next unfound difference.
     * Body: { levelNumber: int }
     */
    @PostMapping("/game/hint")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleHint(
            @RequestBody Map<String, Object> body,
            HttpSession session) {

        GameSession gameSession = (GameSession) session.getAttribute("gameSession");
        if (gameSession == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Session expirée"));
        }

        int levelNumber = ((Number) body.get("levelNumber")).intValue();

        Map<String, Object> result = gameService.getHint(gameSession, levelNumber);

        return ResponseEntity.ok(result);
    }

    /**
     * Next level redirect after completion.
     */
    @GetMapping("/game/next/{currentLevel}")
    public String nextLevel(@PathVariable int currentLevel, HttpSession session) {
        int next = currentLevel + 1;
        if (next > levelService.getTotalLevels())
            return "redirect:/game/victory";
        GameSession gs = new GameSession();
        gs.reset(next);
        session.setAttribute("gameSession", gs);
        return "redirect:/game/" + next;
    }

    /**
     * Victory screen after all levels completed.
     */
    @GetMapping("/game/victory")
    public String victoryPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        playerService.findByUsername(userDetails.getUsername()).ifPresent(p -> model.addAttribute("player", p));
        model.addAttribute("leaderboard", playerService.getLeaderboard());
        return "game/victory";
    }
}
