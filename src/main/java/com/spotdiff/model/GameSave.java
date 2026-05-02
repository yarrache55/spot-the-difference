package com.spotdiff.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "game_saves")
public class GameSave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(nullable = false)
    private int level;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false)
    private int differencesFound;

    @Column(nullable = false)
    private long timeElapsedSeconds;

    @Column(nullable = false)
    private LocalDateTime savedAt;

    @Column(nullable = false)
    private boolean completed = false;

    public GameSave() {
    }

    @PrePersist
    public void prePersist() {
        this.savedAt = LocalDateTime.now();
    }

    public GameSave(Player player, int level, int score, int differencesFound, long timeElapsedSeconds) {
        this.player = player;
        this.level = level;
        this.score = score;
        this.differencesFound = differencesFound;
        this.timeElapsedSeconds = timeElapsedSeconds;
        this.savedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getDifferencesFound() {
        return differencesFound;
    }

    public void setDifferencesFound(int differencesFound) {
        this.differencesFound = differencesFound;
    }

    public long getTimeElapsedSeconds() {
        return timeElapsedSeconds;
    }

    public void setTimeElapsedSeconds(long timeElapsedSeconds) {
        this.timeElapsedSeconds = timeElapsedSeconds;
    }

    public LocalDateTime getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(LocalDateTime savedAt) {
        this.savedAt = savedAt;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
