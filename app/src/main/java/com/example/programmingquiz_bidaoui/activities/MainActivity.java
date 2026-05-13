package com.example.programmingquiz_bidaoui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.programmingquiz_bidaoui.R;
import com.example.programmingquiz_bidaoui.firebase.FirebaseAuthManager;
import com.example.programmingquiz_bidaoui.firebase.FirestoreManager;
import com.example.programmingquiz_bidaoui.models.User;
import com.example.programmingquiz_bidaoui.utils.DatabaseSeeder;

import android.content.SharedPreferences;

public class MainActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private ImageView ivProfile, ivLeaderboard;
    private Spinner spinnerCategory, spinnerLevel;
    private Switch switchCodeChallenge;
    private Button btnStartQuiz, btnAdmin;
    private FirebaseAuthManager authManager;
    private FirestoreManager firestoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        SharedPreferences prefs = getSharedPreferences("ThemePrefs", MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);
        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_main);

        authManager = new FirebaseAuthManager();
        firestoreManager = new FirestoreManager();

        // Ensure database is seeded with latest schema changes
        DatabaseSeeder.seedQuestions(firestoreManager);

        if (authManager.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return;
        }

        tvWelcome = findViewById(R.id.tvWelcome);
        ivProfile = findViewById(R.id.ivProfile);
        ivLeaderboard = findViewById(R.id.ivLeaderboard);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerLevel = findViewById(R.id.spinnerLevel);
        switchCodeChallenge = findViewById(R.id.switchCodeChallenge);
        btnStartQuiz = findViewById(R.id.btnStartQuiz);
        btnAdmin = findViewById(R.id.btnAdmin);
        FloatingActionButton fabChat = findViewById(R.id.fabChat);

        loadUserData();
        setupSpinners();

        ivProfile.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        });

        ivLeaderboard.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LeaderboardActivity.class));
        });

        btnStartQuiz.setOnClickListener(v -> {
            String selectedCategory = spinnerCategory.getSelectedItem().toString();
            String selectedLevel = spinnerLevel.getSelectedItem().toString();
            boolean isCodeChallenge = switchCodeChallenge.isChecked();

            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
            intent.putExtra("CATEGORY", selectedCategory);
            intent.putExtra("LEVEL", selectedLevel);
            intent.putExtra("IS_CODE_CHALLENGE", isCodeChallenge);
            startActivity(intent);
        });

        fabChat.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ChatActivity.class));
        });
    }

    private void loadUserData() {
        String userId = authManager.getCurrentUser().getUid();
        firestoreManager.getUser(userId).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                User user = documentSnapshot.toObject(User.class);
                if (user != null) {
                    tvWelcome.setText("Welcome, " + user.getName() + "!");
                    if (user.isAdmin()) {
                        android.view.View btnAdmin = findViewById(R.id.btnAdmin);
                        btnAdmin.setVisibility(android.view.View.VISIBLE);
                        btnAdmin.setOnClickListener(v -> {
                            startActivity(new Intent(MainActivity.this, AdminDashboardActivity.class));
                        });
                    }
                }
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(MainActivity.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupSpinners() {
        String[] categories = { "Java", "Python", "Web", "C++", "PHP" };
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                categories);
        spinnerCategory.setAdapter(categoryAdapter);

        String[] levels = { "Facile", "Moyen", "Difficile" };
        ArrayAdapter<String> levelAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                levels);
        spinnerLevel.setAdapter(levelAdapter);
    }
}
