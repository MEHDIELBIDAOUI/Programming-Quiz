package com.example.programmingquiz_bidaoui.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.programmingquiz_bidaoui.R;
import com.example.programmingquiz_bidaoui.firebase.FirestoreManager;
import com.example.programmingquiz_bidaoui.models.User;
import com.example.programmingquiz_bidaoui.utils.AdminUserAdapter;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminUsersActivity extends AppCompatActivity {

    private RecyclerView rvUsers;
    private AdminUserAdapter adapter;
    private FirestoreManager firestoreManager;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_users);

        rvUsers = findViewById(R.id.rvUsers);
        firestoreManager = new FirestoreManager();
        userList = new ArrayList<>();

        adapter = new AdminUserAdapter(userList, this::showDeleteDialog);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        rvUsers.setAdapter(adapter);

        loadUsers();
    }

    private void loadUsers() {
        firestoreManager.getDb().collection("users").get().addOnSuccessListener(queryDocumentSnapshots -> {
            userList.clear();
            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                User u = doc.toObject(User.class);
                if (u != null) {
                    userList.add(u);
                }
            }
            adapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to load users", Toast.LENGTH_SHORT).show();
        });
    }

    private void showDeleteDialog(User user) {
        if (user.isAdmin()) {
            Toast.makeText(this, "Impossible de supprimer un administrateur.", Toast.LENGTH_SHORT).show();
            return;
        }
        
        new AlertDialog.Builder(this)
                .setTitle("Supprimer l'utilisateur")
                .setMessage("Êtes-vous sûr de vouloir supprimer d" + "\u00e9" + "finitivement " + user.getName() + " ?")
                .setPositiveButton("Supprimer", (dialog, which) -> deleteUser(user))
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void deleteUser(User user) {
        firestoreManager.getDb().collection("users").document(user.getUserId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Utilisateur supprimé", Toast.LENGTH_SHORT).show();
                    loadUsers();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                });
    }
}
