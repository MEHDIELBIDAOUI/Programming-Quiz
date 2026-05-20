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
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.util.Base64;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import java.io.ByteArrayOutputStream;
import android.widget.ImageView;

public class ProfileActivity extends AppCompatActivity {

    private ImageView ivBack, ivAvatar, fabCamera;
    private TextView tvName, tvEmail, tvTotalScore, tvLevel, tvStreak, tvGamesPlayed;
    private Button btnLogout, btnLocation;
    private Switch switchTheme;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private FirebaseAuthManager authManager;
    private FirestoreManager firestoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ivBack = findViewById(R.id.ivBack);
        ivAvatar = findViewById(R.id.ivAvatar);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvTotalScore = findViewById(R.id.tvTotalScore);
        tvLevel = findViewById(R.id.tvLevel);
        tvStreak = findViewById(R.id.tvStreak);
        tvGamesPlayed = findViewById(R.id.tvGamesPlayed);
        btnLogout = findViewById(R.id.btnLogout);
        btnLocation = findViewById(R.id.btnLocation);
        switchTheme = findViewById(R.id.switchTheme);
        fabCamera = findViewById(R.id.fabCamera);

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        if (imageBitmap != null) {
                            ivAvatar.setImageTintList(null);
                            ivAvatar.setImageBitmap(imageBitmap);
                            saveProfileImage(imageBitmap);
                        }
                    }
                }
        );

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        openCamera();
                    } else {
                        Toast.makeText(this, "Permission caméra requise", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        fabCamera.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA);
            }
        });

        authManager = new FirebaseAuthManager();
        firestoreManager = new FirestoreManager();

        loadProfileData();
        loadProfileImage();

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

        btnLocation.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MapsActivity.class);
            startActivity(intent);
        });

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

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(takePictureIntent);
    }

    private void saveProfileImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

        SharedPreferences prefs = getSharedPreferences("ProfilePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("profile_image", encodedImage);
        editor.apply();
    }

    private void loadProfileImage() {
        SharedPreferences prefs = getSharedPreferences("ProfilePrefs", MODE_PRIVATE);
        String encodedImage = prefs.getString("profile_image", null);
        if (encodedImage != null) {
            byte[] b = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            ivAvatar.setImageTintList(null);
            ivAvatar.setImageBitmap(bitmap);
        }
    }
}
