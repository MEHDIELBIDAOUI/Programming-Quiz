package com.example.programmingquiz_bidaoui.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthManager {
    private FirebaseAuth mAuth;

    public FirebaseAuthManager() {
        mAuth = FirebaseAuth.getInstance();
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public Task<AuthResult> registerUser(String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> loginUser(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    public void logoutUser() {
        mAuth.signOut();
    }
}
