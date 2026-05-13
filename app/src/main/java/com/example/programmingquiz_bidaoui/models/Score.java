package com.example.programmingquiz_bidaoui.models;

public class Score {
    private String id;
    private String userId;
    private int score;
    private int totalQuestions;
    private long timestamp;
    private String category;
    private String level;

    public Score() {
        // Default constructor
    }

    public Score(String id, String userId, int score, int totalQuestions, long timestamp, String category, String level) {
        this.id = id;
        this.userId = userId;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.timestamp = timestamp;
        this.category = category;
        this.level = level;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
}
