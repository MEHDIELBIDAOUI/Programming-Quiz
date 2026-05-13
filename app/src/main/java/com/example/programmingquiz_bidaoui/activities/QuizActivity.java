package com.example.programmingquiz_bidaoui.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.ToneGenerator;

import com.example.programmingquiz_bidaoui.R;
import com.example.programmingquiz_bidaoui.firebase.FirestoreManager;
import com.example.programmingquiz_bidaoui.models.Question;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuizActivity extends AppCompatActivity {

    private TextView tvTimer, tvQuestionCount, tvQuestion;
    private ProgressBar progressBar, pbQuizProgress;
    private Button btnOption1, btnOption2, btnOption3, btnOption4, btnNext;
    private ToneGenerator toneGenerator;
    private FirestoreManager firestoreManager;
    private List<Question> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private CountDownTimer timer;
    private Set<Integer> selectedOptions = new HashSet<>();
    private String category;
    private String level;
    private boolean isCodeChallengeMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        category = getIntent().getStringExtra("CATEGORY");
        level = getIntent().getStringExtra("LEVEL");
        isCodeChallengeMode = getIntent().getBooleanExtra("IS_CODE_CHALLENGE", false);

        tvTimer = findViewById(R.id.tvTimer);
        tvQuestionCount = findViewById(R.id.tvQuestionCount);
        tvQuestion = findViewById(R.id.tvQuestion);
        progressBar = findViewById(R.id.progressBar);
        pbQuizProgress = findViewById(R.id.pbQuizProgress);
        btnOption1 = findViewById(R.id.btnOption1);
        btnOption2 = findViewById(R.id.btnOption2);
        btnOption3 = findViewById(R.id.btnOption3);
        btnOption4 = findViewById(R.id.btnOption4);
        btnNext = findViewById(R.id.btnNext);

        toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);

        firestoreManager = new FirestoreManager();
        questionList = new ArrayList<>();

        loadQuestions();

        View.OnClickListener optionClickListener = v -> {
            int clickedOption = -1;
            if (v == btnOption1) clickedOption = 1;
            else if (v == btnOption2) clickedOption = 2;
            else if (v == btnOption3) clickedOption = 3;
            else if (v == btnOption4) clickedOption = 4;

            if (selectedOptions.contains(clickedOption)) {
                // Toggle off: remove from selection
                selectedOptions.remove(clickedOption);
                int defaultColor = getResources().getColor(R.color.surface_variant, getTheme());
                v.setBackgroundTintList(ColorStateList.valueOf(defaultColor));
            } else {
                // Toggle on: add to selection
                selectedOptions.add(clickedOption);
                v.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#3700B3"))); // selection color
            }
            
            // Show "Next" if at least one option is selected
            btnNext.setVisibility(selectedOptions.isEmpty() ? View.GONE : View.VISIBLE);
        };

        btnOption1.setOnClickListener(optionClickListener);
        btnOption2.setOnClickListener(optionClickListener);
        btnOption3.setOnClickListener(optionClickListener);
        btnOption4.setOnClickListener(optionClickListener);

        btnNext.setOnClickListener(v -> checkAnswerAndProceed());
    }

    private void loadQuestions() {
        firestoreManager.getQuestions(category, level, isCodeChallengeMode).addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                Question q = doc.toObject(Question.class);
                if (q != null) questionList.add(q);
            }

            if (questionList.isEmpty()) {
                Toast.makeText(this, "No questions found for this category and level.", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Collections.shuffle(questionList); // randomize questions
                showQuestion();
            }
        }).addOnFailureListener(e -> {
            android.util.Log.e("QuizActivity", "Error loading questions", e);
            String errorMessage = e.getMessage() != null ? e.getMessage() : "Unknown error occurred.";
            
            if (errorMessage.contains("PERMISSION_DENIED")) {
                errorMessage = "Firebase is blocking access.\n\nYou must go to the Firebase Console website -> Firestore Database -> Rules, and change your rules to:\n\nallow read, write: if true;\n\n(Then click Publish). I cannot do this for you as it requires your Google account login.";
            }

            new androidx.appcompat.app.AlertDialog.Builder(QuizActivity.this)
                    .setTitle("Security Rules Error")
                    .setMessage(errorMessage)
                    .setPositiveButton("OK", (dialog, which) -> finish())
                    .setCancelable(false)
                    .show();
        });
    }

    private void showQuestion() {
        if (currentQuestionIndex < questionList.size()) {
            resetOptionStyles();
            selectedOptions.clear();
            btnNext.setVisibility(View.GONE);

            Question currentQuestion = questionList.get(currentQuestionIndex);
            
            tvQuestionCount.setText("Question " + (currentQuestionIndex + 1) + "/" + questionList.size());
            tvQuestion.setText(currentQuestion.getQuestion());

            int progressPercent = (int) (((float)(currentQuestionIndex + 1) / questionList.size()) * 100);
            pbQuizProgress.setProgress(progressPercent);

            if (currentQuestion.isCodeChallenge()) {
                tvQuestion.setBackgroundColor(Color.parseColor("#1E1E1E"));
                tvQuestion.setTextColor(Color.parseColor("#DCDCAA")); // A nice VSCode-like variable color
                tvQuestion.setTypeface(android.graphics.Typeface.MONOSPACE);
                tvQuestion.setPadding(24, 24, 24, 24);
                tvQuestion.setTextSize(16);
            } else {
                tvQuestion.setBackgroundColor(Color.TRANSPARENT);
                tvQuestion.setTextColor(getResources().getColor(R.color.on_background, getTheme()));
                tvQuestion.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
                tvQuestion.setPadding(0, 0, 0, 0);
                tvQuestion.setTextSize(22);
            }
            btnOption1.setText(currentQuestion.getOption1());
            btnOption2.setText(currentQuestion.getOption2());
            btnOption3.setText(currentQuestion.getOption3());
            btnOption4.setText(currentQuestion.getOption4());
            
            enableOptions();
            startTimer();
        } else {
            finishQuiz();
        }
    }

    private void startTimer() {
        if (timer != null) timer.cancel();
        progressBar.setMax(15);
        progressBar.setProgress(15);

        timer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                tvTimer.setText(secondsRemaining + "s");
                progressBar.setProgress(secondsRemaining);
            }

            @Override
            public void onFinish() {
                tvTimer.setText("0s");
                progressBar.setProgress(0);
                autoProceed();
            }
        }.start();
    }
    
    private void autoProceed() {
        // Time ran out, answer is counted as incorrect, auto proceed
        disableOptions();
        checkAnswer();
        postDelayedProceed();
    }

    private void checkAnswerAndProceed() {
        if (timer != null) timer.cancel();
        disableOptions();
        checkAnswer();
        postDelayedProceed();
    }

    private void postDelayedProceed() {
        btnNext.setVisibility(View.GONE);
        tvQuestion.postDelayed(() -> {
            currentQuestionIndex++;
            showQuestion();
        }, 2000); // Wait 2 seconds before showing next question
    }

    private void checkAnswer() {
        Question currentQuestion = questionList.get(currentQuestionIndex);
        int correctOption = currentQuestion.getCorrectAnswer();

        // For single-correct-answer questions, selection is correct only if 
        // exactly one option is selected and it matches the correct answer.
        boolean isCorrect = selectedOptions.size() == 1 && selectedOptions.contains(correctOption);

        if (isCorrect) {
            score++;
            if (toneGenerator != null) {
                toneGenerator.startTone(ToneGenerator.TONE_PROP_ACK, 100);
            }
        } else {
            if (toneGenerator != null) {
                toneGenerator.startTone(ToneGenerator.TONE_PROP_NACK, 150);
            }
        }

        // Highlight correct answer in green
        highlightButton(correctOption, true);
        
        // Highlight all selected wrong answers in red
        for (int option : selectedOptions) {
            if (option != correctOption) {
                highlightButton(option, false);
            }
        }
    }

    private void highlightButton(int optionIndex, boolean isCorrect) {
        Button btn = null;
        if (optionIndex == 1) btn = btnOption1;
        if (optionIndex == 2) btn = btnOption2;
        if (optionIndex == 3) btn = btnOption3;
        if (optionIndex == 4) btn = btnOption4;

        if (btn != null) {
            btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(isCorrect ? "#4CAF50" : "#F44336")));
        }
    }

    private void resetOptionStyles() {
        int defaultColor = getResources().getColor(R.color.surface_variant, getTheme());
        btnOption1.setBackgroundTintList(ColorStateList.valueOf(defaultColor));
        btnOption2.setBackgroundTintList(ColorStateList.valueOf(defaultColor));
        btnOption3.setBackgroundTintList(ColorStateList.valueOf(defaultColor));
        btnOption4.setBackgroundTintList(ColorStateList.valueOf(defaultColor));
    }

    private void disableOptions() {
        btnOption1.setEnabled(false);
        btnOption2.setEnabled(false);
        btnOption3.setEnabled(false);
        btnOption4.setEnabled(false);
    }

    private void enableOptions() {
        btnOption1.setEnabled(true);
        btnOption2.setEnabled(true);
        btnOption3.setEnabled(true);
        btnOption4.setEnabled(true);
    }

    private void finishQuiz() {
        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
        intent.putExtra("SCORE", score);
        intent.putExtra("TOTAL", questionList.size());
        intent.putExtra("CATEGORY", category);
        intent.putExtra("LEVEL", level);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
        if (toneGenerator != null) {
            toneGenerator.release();
            toneGenerator = null;
        }
    }
}
