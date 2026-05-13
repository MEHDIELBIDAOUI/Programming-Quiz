package com.example.programmingquiz_bidaoui.firebase;

import com.example.programmingquiz_bidaoui.models.Question;
import com.example.programmingquiz_bidaoui.models.Score;
import com.example.programmingquiz_bidaoui.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class FirestoreManager {
    private FirebaseFirestore db;
    private final String COLLECTION_USERS = "users";
    private final String COLLECTION_QUESTIONS = "questions";
    private final String COLLECTION_SCORES = "scores";

    public FirestoreManager() {
        db = FirebaseFirestore.getInstance();
    }

    public Task<Void> saveUser(User user) {
        return db.collection(COLLECTION_USERS).document(user.getUserId()).set(user);
    }

    public Task<DocumentSnapshot> getUser(String userId) {
        return db.collection(COLLECTION_USERS).document(userId).get();
    }

    public Task<QuerySnapshot> getQuestions(String category, String level, boolean isCodeChallenge) {
        if (isCodeChallenge) {
            return db.collection(COLLECTION_QUESTIONS)
                    .whereEqualTo("category", category)
                    .whereEqualTo("level", level)
                    .whereEqualTo("codeChallenge", true)
                    .get();
        } else {
            return db.collection(COLLECTION_QUESTIONS)
                    .whereEqualTo("category", category)
                    .whereEqualTo("level", level)
                    .get();
        }
    }

    public Task<QuerySnapshot> getTopUsers(int limit) {
        return db.collection(COLLECTION_USERS)
                 .orderBy("totalScore", com.google.firebase.firestore.Query.Direction.DESCENDING)
                 .limit(limit)
                 .get();
    }

    public Task<Void> saveScore(Score score) {
        return db.collection(COLLECTION_SCORES).document(score.getId()).set(score);
    }

    public Task<QuerySnapshot> getUserHistory(String userId) {
        return db.collection(COLLECTION_SCORES)
                .whereEqualTo("userId", userId)
                //.orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get();
    }

    public Task<Void> updateUserStats(String userId, int additionalScore) {
        return db.collection(COLLECTION_USERS).document(userId).get().continueWithTask(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                User user = task.getResult().toObject(User.class);
                if (user != null) {
                    Map<String, Object> updates = new HashMap<>();
                    
                    int newScore = user.getTotalScore() + additionalScore;
                    updates.put("totalScore", newScore);

                    updates.put("gamesPlayed", user.getGamesPlayed() + 1);

                    long lastPlayed = user.getLastPlayedTimestamp();
                    long now = System.currentTimeMillis();
                    int streak = user.getCurrentStreak();
                    
                    // Basic 24h streak logic
                    if (lastPlayed == 0) {
                        streak = 1;
                    } else if (now - lastPlayed > 86400000L * 2) {
                        streak = 1; // missed a day, reset reset
                    } else if (now - lastPlayed > 86400000L) {
                        streak += 1; // played next day, increment
                    } // else same day, keep streak as is

                    updates.put("currentStreak", streak);
                    updates.put("lastPlayedTimestamp", now);

                    // Dynamic Levels
                    String level = "Beginner";
                    if (newScore >= 2000) level = "Expert";
                    else if (newScore >= 500) level = "Intermediate";
                    updates.put("userLevel", level);

                    return db.collection(COLLECTION_USERS).document(userId).update(updates);
                }
            }
            return null;
        });
    }

    public FirebaseFirestore getDb() {
        return db;
    }
}
