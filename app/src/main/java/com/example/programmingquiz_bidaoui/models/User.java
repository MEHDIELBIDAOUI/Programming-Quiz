package com.example.programmingquiz_bidaoui.models;

public class User {
    private String userId;
    private String name;
    private String email;
    private int totalScore;
    private int currentStreak;
    private long lastPlayedTimestamp;
    private int gamesPlayed;
    private String userLevel;
    private boolean isAdmin;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userId, String name, String email, int totalScore) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.totalScore = totalScore;
        this.currentStreak = 0;
        this.lastPlayedTimestamp = 0;
        this.gamesPlayed = 0;
        this.userLevel = "Beginner";
        this.isAdmin = email != null && email.equals("admin@admin.com");
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getTotalScore() { return totalScore; }
    public void setTotalScore(int totalScore) { this.totalScore = totalScore; }

    public int getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(int currentStreak) { this.currentStreak = currentStreak; }

    public long getLastPlayedTimestamp() { return lastPlayedTimestamp; }
    public void setLastPlayedTimestamp(long lastPlayedTimestamp) { this.lastPlayedTimestamp = lastPlayedTimestamp; }

    public int getGamesPlayed() { return gamesPlayed; }
    public void setGamesPlayed(int gamesPlayed) { this.gamesPlayed = gamesPlayed; }

    public String getUserLevel() { return userLevel; }
    public void setUserLevel(String userLevel) { this.userLevel = userLevel; }

    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean admin) { isAdmin = admin; }
}
