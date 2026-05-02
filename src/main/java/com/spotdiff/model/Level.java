package com.spotdiff.model;

import java.util.List;

/**
 * Represents a game level with its two images and list of differences.
 */
public class Level {

    private final int number;
    private final String title;
    private final String description;
    private final String originalImage;    // path under /images/
    private final String modifiedImage;   // path under /images/
    private final List<Difference> differences;
    private final int timeLimit;          // seconds; 0 = no limit
    private final int pointsPerDiff;

    public Level(int number, String title, String description,
                 String originalImage, String modifiedImage,
                 List<Difference> differences, int timeLimit, int pointsPerDiff) {
        this.number = number;
        this.title = title;
        this.description = description;
        this.originalImage = originalImage;
        this.modifiedImage = modifiedImage;
        this.differences = differences;
        this.timeLimit = timeLimit;
        this.pointsPerDiff = pointsPerDiff;
    }

    public int getNumber() { return number; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getOriginalImage() { return originalImage; }
    public String getModifiedImage() { return modifiedImage; }
    public List<Difference> getDifferences() { return differences; }
    public int getTimeLimit() { return timeLimit; }
    public int getPointsPerDiff() { return pointsPerDiff; }
    public int getTotalDifferences() { return differences.size(); }
}
