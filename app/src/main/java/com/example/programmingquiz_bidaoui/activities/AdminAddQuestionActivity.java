package com.example.programmingquiz_bidaoui.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.programmingquiz_bidaoui.R;
import com.example.programmingquiz_bidaoui.firebase.FirestoreManager;
import com.example.programmingquiz_bidaoui.models.Question;

import java.util.UUID;

public class AdminAddQuestionActivity extends AppCompatActivity {

    private Switch switchIsCodeChallenge;
    private EditText etQuestion, etOption1, etOption2, etOption3, etOption4;
    private Spinner spinnerCorrectAnswer, spinnerCategory, spinnerLevel;
    private Button btnSave;
    private FirestoreManager firestoreManager;
    private String editingQuestionId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_question);

        switchIsCodeChallenge = findViewById(R.id.switchIsCodeChallenge);
        etQuestion = findViewById(R.id.etQuestion);
        etOption1 = findViewById(R.id.etOption1);
        etOption2 = findViewById(R.id.etOption2);
        etOption3 = findViewById(R.id.etOption3);
        etOption4 = findViewById(R.id.etOption4);
        spinnerCorrectAnswer = findViewById(R.id.spinnerCorrectAnswer);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerLevel = findViewById(R.id.spinnerLevel);
        btnSave = findViewById(R.id.btnSaveQuestion);

        firestoreManager = new FirestoreManager();

        setupSpinners();

        boolean isCodeChallenge = getIntent().getBooleanExtra("IS_CODE_CHALLENGE", false);
        switchIsCodeChallenge.setChecked(isCodeChallenge);

        btnSave.setOnClickListener(v -> saveQuestion());

        switchIsCodeChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etQuestion.setHint("Entrez le bloc de code ici...");
                etQuestion.setTypeface(android.graphics.Typeface.MONOSPACE);
            } else {
                etQuestion.setHint("Question");
                etQuestion.setTypeface(android.graphics.Typeface.DEFAULT);
            }
        });

        // Check if we are in Edit Mode
        if (getIntent().hasExtra("question")) {
            Question q = (Question) getIntent().getSerializableExtra("question");
            if (q != null) {
                editingQuestionId = q.getId();
                btnSave.setText("Modifier la Question");

                etQuestion.setText(q.getQuestion());
                etOption1.setText(q.getOption1());
                etOption2.setText(q.getOption2());
                etOption3.setText(q.getOption3());
                etOption4.setText(q.getOption4());

                switchIsCodeChallenge.setChecked(q.isCodeChallenge());

                // Set spinners
                spinnerCorrectAnswer.setSelection(q.getCorrectAnswer() - 1);

                String[] categories = {"Java", "Python", "Web", "C++", "PHP"};
                for (int i = 0; i < categories.length; i++) {
                    if (categories[i].equals(q.getCategory())) spinnerCategory.setSelection(i);
                }

                String[] levels = {"Facile", "Moyen", "Difficile"};
                for (int i = 0; i < levels.length; i++) {
                    if (levels[i].equals(q.getLevel())) spinnerLevel.setSelection(i);
                }
            }
        }
    }

    private void setupSpinners() {
        String[] answers = {"1", "2", "3", "4"};
        spinnerCorrectAnswer.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, answers));

        String[] categories = {"Java", "Python", "Web", "C++", "PHP"};
        spinnerCategory.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories));

        String[] levels = {"Facile", "Moyen", "Difficile"};
        spinnerLevel.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, levels));
    }

    private void saveQuestion() {
        String qText = etQuestion.getText().toString().trim();
        String o1 = etOption1.getText().toString().trim();
        String o2 = etOption2.getText().toString().trim();
        String o3 = etOption3.getText().toString().trim();
        String o4 = etOption4.getText().toString().trim();

        if (qText.isEmpty() || o1.isEmpty() || o2.isEmpty() || o3.isEmpty() || o4.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        int correctAns = Integer.parseInt(spinnerCorrectAnswer.getSelectedItem().toString());
        String cat = spinnerCategory.getSelectedItem().toString();
        String lvl = spinnerLevel.getSelectedItem().toString();
        boolean isCode = switchIsCodeChallenge.isChecked();

        String targetId = (editingQuestionId != null) ? editingQuestionId : UUID.randomUUID().toString();
        Question newQuestion = new Question(targetId, qText, o1, o2, o3, o4, correctAns, cat, lvl, isCode);

        // Save to firestore under 'questions' collection natively
        firestoreManager.getDb().collection("questions").document(targetId).set(newQuestion)
                .addOnSuccessListener(aVoid -> {
                    String msg = (editingQuestionId != null) ? "Question modifiée avec succès !" : "Question ajoutée avec succès !";
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    finish(); // Go back to list
                })
                .addOnFailureListener(e -> {
                    String msg = (editingQuestionId != null) ? "Erreur lors de la modification" : "Erreur lors de l'ajout";
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                });
    }

    private void switchIsCodeChangeListener(android.widget.CompoundButton.OnCheckedChangeListener listener) {
        switchIsCodeChallenge.setOnCheckedChangeListener(listener);
    }
}
