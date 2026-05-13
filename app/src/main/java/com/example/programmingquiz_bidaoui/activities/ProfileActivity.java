package com.example.programmingquiz_bidaoui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.programmingquiz_bidaoui.R;
import com.example.programmingquiz_bidaoui.firebase.FirebaseAuthManager;
import com.example.programmingquiz_bidaoui.firebase.FirestoreManager;
import com.example.programmingquiz_bidaoui.models.User;
import android.content.SharedPreferences;
import android.widget.Switch;

public class ProfileActivity extends AppCompatActivity {

    private ImageView ivBack;
    private TextView tvName, tvEmail, tvTotalScore, tvLevel, tvStreak, tvGamesPlayed;
    private Button btnLogout;
    private Switch switchTheme;
    private FirebaseAuthManager authManager;
    private FirestoreManager firestoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ivBack = findViewById(R.id.ivBack);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvTotalScore = findViewById(R.id.tvTotalScore);
        tvLevel = findViewById(R.id.tvLevel);
        tvStreak = findViewById(R.id.tvStreak);
        tvGamesPlayed = findViewById(R.id.tvGamesPlayed);
        btnLogout = findViewById(R.id.btnLogout);
        switchTheme = findViewById(R.id.switchTheme);

        authManager = new FirebaseAuthManager();
        firestoreManager = new FirestoreManager();

        loadProfileData();

        SharedPreferences prefs = getSharedPreferences("ThemePrefs", MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false); // Assuming light by default or depending on system
        switchTheme.setChecked(isDark);

        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isDark", isChecked);
            editor.apply();

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        ivBack.setOnClickListener(v -> finish());

        btnLogout.setOnClickListener(v -> {
            authManager.logoutUser();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void loadProfileData() {
        if (authManager.getCurrentUser() != null) {
            String userId = authManager.getCurrentUser().getUid();
            firestoreManager.getUser(userId).addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    User user = documentSnapshot.toObject(User.class);
                    if (user != null) {
                        tvName.setText(user.getName());
                        tvEmail.setText(user.getEmail());
                        tvTotalScore.setText(String.valueOf(user.getTotalScore()));
                        tvLevel.setText(user.getUserLevel() != null ? user.getUserLevel() : "Beginner");
                        tvStreak.setText(user.getCurrentStreak() + " Jours");
                        tvGamesPlayed.setText(String.valueOf(user.getGamesPlayed()));
                    }
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(ProfileActivity.this, "Failed to load profile data", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
