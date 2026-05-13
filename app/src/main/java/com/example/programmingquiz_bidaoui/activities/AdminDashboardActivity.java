package com.example.programmingquiz_bidaoui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.programmingquiz_bidaoui.R;
import com.example.programmingquiz_bidaoui.firebase.FirestoreManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.example.programmingquiz_bidaoui.models.User;

public class AdminDashboardActivity extends AppCompatActivity {

    private TextView tvTotalUsers, tvTopScore, tvTotalQuestions, tvTotalCodeChallenges;
    private Button btnAddQuestion, btnAddCodeChallenge, btnManageQuestions, btnManageUsers;
    private FirestoreManager firestoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        tvTotalUsers = findViewById(R.id.tvTotalUsers);
        tvTopScore = findViewById(R.id.tvTopScore);
        tvTotalQuestions = findViewById(R.id.tvTotalQuestions);
        tvTotalCodeChallenges = findViewById(R.id.tvTotalCodeChallenges);
        
        btnAddQuestion = findViewById(R.id.btnAddQuestion);
        btnAddCodeChallenge = findViewById(R.id.btnAddCodeChallenge);
        btnManageQuestions = findViewById(R.id.btnManageQuestions);
        btnManageUsers = findViewById(R.id.btnManageUsers);

        firestoreManager = new FirestoreManager();

        loadStats();

        btnAddQuestion.setOnClickListener(v -> {
            startActivity(new Intent(AdminDashboardActivity.this, AdminAddQuestionActivity.class));
        });

        btnAddCodeChallenge.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminAddQuestionActivity.class);
            intent.putExtra("IS_CODE_CHALLENGE", true);
            startActivity(intent);
        });

        btnManageQuestions.setOnClickListener(v -> {
            startActivity(new Intent(AdminDashboardActivity.this, AdminQuestionsActivity.class));
        });

        btnManageUsers.setOnClickListener(v -> {
            startActivity(new Intent(AdminDashboardActivity.this, AdminUsersActivity.class));
        });
    }

    private void loadStats() {
        firestoreManager.getDb().collection("users").get().addOnSuccessListener(queryDocumentSnapshots -> {
            tvTotalUsers.setText(String.valueOf(queryDocumentSnapshots.size()));
            
            int topScore = 0;
            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                User u = doc.toObject(User.class);
                if (u != null) {
                    topScore = Math.max(topScore, u.getTotalScore());
                }
            }
            tvTopScore.setText(String.valueOf(topScore));
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to load users stats", Toast.LENGTH_SHORT).show();
        });

        firestoreManager.getDb().collection("questions").get().addOnSuccessListener(queryDocumentSnapshots -> {
            int qCount = 0;
            int codeCount = 0;
            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                Boolean isCode = doc.getBoolean("isCodeChallenge");
                if (isCode != null && isCode) {
                    codeCount++;
                } else {
                    qCount++;
                }
            }
            tvTotalQuestions.setText(String.valueOf(qCount));
            tvTotalCodeChallenges.setText(String.valueOf(codeCount));
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to load questions stats", Toast.LENGTH_SHORT).show();
        });
    }
}
