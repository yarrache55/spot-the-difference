package com.spotdiff.model;

/**
 * Represents a difference zone on the modified image.
 * Coordinates are percentages (0-100) relative to image size,
 * so they work on any screen resolution.
 *
 * Supports both circular tolerance zones and exact rectangular hit zones.
 */
public class Difference {

    private final String id;
    private final double xPercent; // center X in %
    private final double yPercent; // center Y in %
    private final double radiusPercent; // click tolerance radius in %
    private final boolean rectangular;
    private final double xMinPercent;
    private final double yMinPercent;
    private final double xMaxPercent;
    private final double yMaxPercent;
    private boolean found = false;

    public Difference(String id, double xPercent, double yPercent, double radiusPercent) {
        this.id = id;
        this.xPercent = xPercent;
        this.yPercent = yPercent;
        this.radiusPercent = radiusPercent;
        this.rectangular = false;
        this.xMinPercent = 0;
        this.yMinPercent = 0;
        this.xMaxPercent = 0;
        this.yMaxPercent = 0;
    }

    public Difference(String id,
            int x1, int y1, int x2, int y2,
            int imageWidth, int imageHeight) {
        this.id = id;
        this.xMinPercent = x1 * 100.0 / imageWidth;
        this.yMinPercent = y1 * 100.0 / imageHeight;
        this.xMaxPercent = x2 * 100.0 / imageWidth;
        this.yMaxPercent = y2 * 100.0 / imageHeight;
        this.xPercent = ((x1 + x2) / 2.0) * 100.0 / imageWidth;
        this.yPercent = ((y1 + y2) / 2.0) * 100.0 / imageHeight;
        this.radiusPercent = 0;
        this.rectangular = true;
    }

    public String getId() {
        return id;
    }

    public double getXPercent() {
        return xPercent;
    }

    public double getYPercent() {
        return yPercent;
    }

    public double getRadiusPercent() {
        return radiusPercent;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    /**
     * Check if a click at (clickXPercent, clickYPercent) hits this difference zone.
     */
    public boolean isHit(double clickXPercent, double clickYPercent) {
        if (rectangular) {
            return clickXPercent >= xMinPercent && clickXPercent <= xMaxPercent
                    && clickYPercent >= yMinPercent && clickYPercent <= yMaxPercent;
        }

        double dx = clickXPercent - xPercent;
        double dy = clickYPercent - yPercent;
        return Math.sqrt(dx * dx + dy * dy) <= radiusPercent;
    }
}
