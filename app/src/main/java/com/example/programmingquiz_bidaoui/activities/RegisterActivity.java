package com.example.programmingquiz_bidaoui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.programmingquiz_bidaoui.R;
import com.example.programmingquiz_bidaoui.firebase.FirebaseAuthManager;
import com.example.programmingquiz_bidaoui.firebase.FirestoreManager;
import com.example.programmingquiz_bidaoui.models.User;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private CheckBox cbAdmin;
    private Button btnRegister;
    private TextView tvLogin;
    private FirebaseAuthManager authManager;
    private FirestoreManager firestoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        authManager = new FirebaseAuthManager();
        firestoreManager = new FirestoreManager();

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        cbAdmin = findViewById(R.id.cbAdmin);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        btnRegister.setOnClickListener(v -> registerUser());

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Name is required");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return;
        }

        btnRegister.setEnabled(false);
        btnRegister.setText("Loading...");

        authManager.registerUser(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = authManager.getCurrentUser();
                if (firebaseUser != null) {
                    User newUser = new User(firebaseUser.getUid(), name, email, 0);
                    if (cbAdmin.isChecked() || "admin@admin.com".equals(email)) {
                        newUser.setAdmin(true);
                    }
                    firestoreManager.saveUser(newUser).addOnCompleteListener(dbTask -> {
                        if (dbTask.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Database Error: " + dbTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            btnRegister.setEnabled(true);
                            btnRegister.setText("Register");
                        }
                    });
                }
            } else {
                Toast.makeText(RegisterActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                btnRegister.setEnabled(true);
                btnRegister.setText("Register");
            }
        });
    }
}
