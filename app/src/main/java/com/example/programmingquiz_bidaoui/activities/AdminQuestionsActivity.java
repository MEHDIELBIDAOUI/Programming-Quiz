package com.example.programmingquiz_bidaoui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.programmingquiz_bidaoui.R;
import com.example.programmingquiz_bidaoui.firebase.FirestoreManager;
import com.example.programmingquiz_bidaoui.models.Question;
import com.example.programmingquiz_bidaoui.utils.AdminQuestionAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminQuestionsActivity extends AppCompatActivity {

    private RecyclerView rvQuestions;
    private FloatingActionButton fabAddQuestion;
    private AdminQuestionAdapter adapter;
    private FirestoreManager firestoreManager;
    private List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_questions);

        rvQuestions = findViewById(R.id.rvQuestions);
        fabAddQuestion = findViewById(R.id.fabAddQuestion);

        firestoreManager = new FirestoreManager();
        questionList = new ArrayList<>();

        adapter = new AdminQuestionAdapter(questionList, new AdminQuestionAdapter.OnQuestionActionListener() {
            @Override
            public void onDeleteClick(Question question) {
                showDeleteDialog(question);
            }

            @Override
            public void onEditClick(Question question) {
                Intent intent = new Intent(AdminQuestionsActivity.this, AdminAddQuestionActivity.class);
                intent.putExtra("question", question);
                startActivity(intent);
            }

            @Override
            public void onPreviewClick(Question question) {
                showPreviewDialog(question);
            }
        });
        rvQuestions.setLayoutManager(new LinearLayoutManager(this));
        rvQuestions.setAdapter(adapter);

        fabAddQuestion.setOnClickListener(v -> {
            startActivity(new Intent(AdminQuestionsActivity.this, AdminAddQuestionActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadQuestions();
    }

    private void loadQuestions() {
        firestoreManager.getDb().collection("questions").get().addOnSuccessListener(queryDocumentSnapshots -> {
            questionList.clear();
            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                Question q = doc.toObject(Question.class);
                if (q != null) {
                    q.setId(doc.getId()); // ensure we have the document ID for deletion
                    questionList.add(q);
                }
            }
            adapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to load questions", Toast.LENGTH_SHORT).show();
        });
    }

    private void showDeleteDialog(Question question) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Question")
                .setMessage("Are you sure you want to delete this question?")
                .setPositiveButton("Delete", (dialog, which) -> deleteQuestion(question))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteQuestion(Question question) {
        if (question.getId() == null) return;
        firestoreManager.getDb().collection("questions").document(question.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                    loadQuestions();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to delete", Toast.LENGTH_SHORT).show();
                });
    }

    private void showPreviewDialog(Question question) {
        View dialogView = android.view.LayoutInflater.from(this).inflate(R.layout.dialog_question_preview, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        android.widget.TextView tvCategory = dialogView.findViewById(R.id.tvPreviewCategory);
        android.widget.TextView tvQuestion = dialogView.findViewById(R.id.tvPreviewQuestion);
        android.widget.TextView tvOp1 = dialogView.findViewById(R.id.tvPreviewOption1);
        android.widget.TextView tvOp2 = dialogView.findViewById(R.id.tvPreviewOption2);
        android.widget.TextView tvOp3 = dialogView.findViewById(R.id.tvPreviewOption3);
        android.widget.TextView tvOp4 = dialogView.findViewById(R.id.tvPreviewOption4);
        android.widget.Button btnClose = dialogView.findViewById(R.id.btnPreviewClose);

        tvCategory.setText(question.getCategory() + " - " + question.getLevel());
        tvQuestion.setText(question.getQuestion());
        tvOp1.setText(question.getOption1());
        tvOp2.setText(question.getOption2());
        tvOp3.setText(question.getOption3());
        tvOp4.setText(question.getOption4());

        // Highlight correct answer
        int correct = question.getCorrectAnswer();
        if (correct == 1) tvOp1.setBackgroundColor(android.graphics.Color.parseColor("#4CAF50"));
        if (correct == 2) tvOp2.setBackgroundColor(android.graphics.Color.parseColor("#4CAF50"));
        if (correct == 3) tvOp3.setBackgroundColor(android.graphics.Color.parseColor("#4CAF50"));
        if (correct == 4) tvOp4.setBackgroundColor(android.graphics.Color.parseColor("#4CAF50"));

        if (question.isCodeChallenge()) {
            tvQuestion.setTypeface(android.graphics.Typeface.MONOSPACE);
        }

        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }
}
