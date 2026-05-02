package com.spotdiff.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds the state of an active game session (stored in HTTP session).
 */
public class GameSession implements java.io.Serializable {

    private int currentLevel = 1;
    private int score = 0;
    private int differencesFound = 0;
    private long startTimeMillis = System.currentTimeMillis();
    private List<String> foundDifferenceIds = new ArrayList<>();

    // For tracking wrong clicks (penalty system)
    private int wrongClicks = 0;
    private static final int WRONG_CLICK_PENALTY = 50;

    public void reset(int level) {
        this.currentLevel = level;
        this.differencesFound = 0;
        this.startTimeMillis = System.currentTimeMillis();
        this.foundDifferenceIds = new ArrayList<>();
        this.wrongClicks = 0;
    }

    public void addFoundDifference(String id, int pointsPerDiff) {
        if (!foundDifferenceIds.contains(id)) {
            foundDifferenceIds.add(id);
            differencesFound++;
            // Bonus: faster = more points (time bonus up to 2x)
            long elapsed = getElapsedSeconds();
            int timeBonus = (int) Math.max(0, (120 - elapsed));
            score += pointsPerDiff + (timeBonus / 10);
        }
    }

    public void registerWrongClick() {
        wrongClicks++;
        score = Math.max(0, score - WRONG_CLICK_PENALTY);
    }

    public long getElapsedSeconds() {
        return (System.currentTimeMillis() - startTimeMillis) / 1000;
    }

    public boolean hasDifferenceBeenFound(String id) {
        return foundDifferenceIds.contains(id);
    }

    // Getters & setters
    public int getCurrentLevel() { return currentLevel; }
    public void setCurrentLevel(int currentLevel) { this.currentLevel = currentLevel; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public int getDifferencesFound() { return differencesFound; }
    public List<String> getFoundDifferenceIds() { return foundDifferenceIds; }
    public int getWrongClicks() { return wrongClicks; }
    public long getStartTimeMillis() { return startTimeMillis; }
    public void setStartTimeMillis(long startTimeMillis) { this.startTimeMillis = startTimeMillis; }
}
