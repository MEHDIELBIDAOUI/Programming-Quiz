package com.example.programmingquiz_bidaoui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.programmingquiz_bidaoui.R;
import com.example.programmingquiz_bidaoui.firebase.FirebaseAuthManager;
import com.example.programmingquiz_bidaoui.firebase.FirestoreManager;
import com.example.programmingquiz_bidaoui.models.Score;

import java.util.UUID;

public class ResultActivity extends AppCompatActivity {

    private TextView tvScore;
    private Button btnRestart, btnHome, btnLogout;
    private FirestoreManager firestoreManager;
    private FirebaseAuthManager authManager;

    private int score;
    private int total;
    private String category;
    private String level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        firestoreManager = new FirestoreManager();
        authManager = new FirebaseAuthManager();

        tvScore = findViewById(R.id.tvScore);
        btnRestart = findViewById(R.id.btnRestart);
        btnHome = findViewById(R.id.btnHome);
        btnLogout = findViewById(R.id.btnLogout);

        score = getIntent().getIntExtra("SCORE", 0);
        total = getIntent().getIntExtra("TOTAL", 10);
        category = getIntent().getStringExtra("CATEGORY");
        level = getIntent().getStringExtra("LEVEL");

        tvScore.setText(score + "/" + total);

        saveScore();

        btnRestart.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, QuizActivity.class);
            intent.putExtra("CATEGORY", category);
            intent.putExtra("LEVEL", level);
            startActivity(intent);
            finish();
        });

        btnHome.setOnClickListener(v -> {
            startActivity(new Intent(ResultActivity.this, MainActivity.class));
            finish();
        });

        btnLogout.setOnClickListener(v -> {
            authManager.logoutUser();
            Intent intent = new Intent(ResultActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void saveScore() {
        if (authManager.getCurrentUser() != null) {
            String userId = authManager.getCurrentUser().getUid();
            String scoreId = UUID.randomUUID().toString();
            long timestamp = System.currentTimeMillis();

            Score newScore = new Score(scoreId, userId, score, total, timestamp, category, level);

            firestoreManager.saveScore(newScore).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    firestoreManager.updateUserStats(userId, score).addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Score saved & Stats updated!", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    Toast.makeText(this, "Failed to save score.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
