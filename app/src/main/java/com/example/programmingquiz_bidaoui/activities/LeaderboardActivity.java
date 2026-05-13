package com.example.programmingquiz_bidaoui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.programmingquiz_bidaoui.R;
import com.example.programmingquiz_bidaoui.firebase.FirestoreManager;
import com.example.programmingquiz_bidaoui.models.User;
import com.example.programmingquiz_bidaoui.utils.UserAdapter;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private ImageView ivBack;
    private RecyclerView rvLeaderboard;
    private ProgressBar pbLoading;
    private FirestoreManager firestoreManager;
    private UserAdapter userAdapter;
    private List<User> topUsersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        ivBack = findViewById(R.id.ivBack);
        rvLeaderboard = findViewById(R.id.rvLeaderboard);
        pbLoading = findViewById(R.id.pbLoading);

        firestoreManager = new FirestoreManager();
        topUsersList = new ArrayList<>();
        
        userAdapter = new UserAdapter(topUsersList);
        rvLeaderboard.setLayoutManager(new LinearLayoutManager(this));
        rvLeaderboard.setAdapter(userAdapter);

        ivBack.setOnClickListener(v -> finish());

        loadLeaderboard();
    }

    private void loadLeaderboard() {
        pbLoading.setVisibility(View.VISIBLE);
        firestoreManager.getTopUsers(15).addOnSuccessListener(queryDocumentSnapshots -> {
            pbLoading.setVisibility(View.GONE);
            topUsersList.clear();
            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                User user = doc.toObject(User.class);
                if (user != null) {
                    topUsersList.add(user);
                }
            }
            userAdapter.notifyDataSetChanged();
            
            if (topUsersList.isEmpty()) {
                Toast.makeText(this, "No users found on leaderboard.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            pbLoading.setVisibility(View.GONE);
            Toast.makeText(this, "Error loading leaderboard", Toast.LENGTH_SHORT).show();
        });
    }
}
