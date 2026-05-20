package com.example.programmingquiz_bidaoui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.programmingquiz_bidaoui.BuildConfig;
import com.example.programmingquiz_bidaoui.R;
import com.example.programmingquiz_bidaoui.adapters.ChatAdapter;
import com.example.programmingquiz_bidaoui.models.ChatMessage;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {

    private static final int MIC_PERMISSION_REQUEST = 2001;

    private RecyclerView recyclerViewChat;
    private EditText etMessage;
    private ImageButton btnSend;
    private ImageButton btnMic;
    private TextView tvListening;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> messageList;
    private OkHttpClient client;

    private SpeechRecognizer speechRecognizer;
    private boolean isListening = false;

    private static final String API_KEY = BuildConfig.GEMINI_API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbarChat);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // Views
        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        etMessage        = findViewById(R.id.etMessage);
        btnSend          = findViewById(R.id.btnSend);
        btnMic           = findViewById(R.id.btnMic);
        tvListening      = findViewById(R.id.tvListening);

        // RecyclerView
        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewChat.setLayoutManager(layoutManager);
        recyclerViewChat.setAdapter(chatAdapter);

        client = new OkHttpClient();

        // Welcome message
        addMessage("Bonjour ! Je suis ton assistant pédagogique en programmation. Comment puis-je t'aider aujourd'hui ?",
                ChatMessage.TYPE_BOT);

        // ── Send button ──
        btnSend.setOnClickListener(v -> {
            String text = etMessage.getText().toString().trim();
            if (!text.isEmpty()) {
                addMessage(text, ChatMessage.TYPE_USER);
                etMessage.setText("");
                sendMessageToGemini();
            }
        });

        // ── Mic button ──
        btnMic.setOnClickListener(v -> {
            if (isListening) {
                stopListening();
            } else {
                if (hasMicPermission()) {
                    startListening();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.RECORD_AUDIO},
                            MIC_PERMISSION_REQUEST);
                }
            }
        });

        // Pre-build SpeechRecognizer
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            speechRecognizer.setRecognitionListener(buildRecognitionListener());
        }
    }

    // ────────────────────────────────────────────────────
    //  Voice Recognition
    // ────────────────────────────────────────────────────

    private boolean hasMicPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void startListening() {
        if (speechRecognizer == null) {
            Toast.makeText(this, "Reconnaissance vocale non disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        isListening = true;
        setMicActive(true);

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "fr-FR");
        intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, false);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

        speechRecognizer.startListening(intent);
    }

    private void stopListening() {
        if (speechRecognizer != null) {
            speechRecognizer.stopListening();
        }
        isListening = false;
        setMicActive(false);
    }

    private void setMicActive(boolean active) {
        runOnUiThread(() -> {
            btnMic.setActivated(active);
            if (active) {
                tvListening.setVisibility(View.VISIBLE);
                startBlinkAnimation(tvListening);
            } else {
                tvListening.setVisibility(View.GONE);
                tvListening.clearAnimation();
            }
        });
    }

    private void startBlinkAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.2f);
        anim.setDuration(600);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setRepeatMode(Animation.REVERSE);
        view.startAnimation(anim);
    }

    private RecognitionListener buildRecognitionListener() {
        return new RecognitionListener() {

            @Override
            public void onReadyForSpeech(Bundle params) {
                runOnUiThread(() ->
                        Toast.makeText(ChatActivity.this, "🎙️ Parlez maintenant...", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onBeginningOfSpeech() { /* speech detected */ }

            @Override
            public void onRmsChanged(float rmsdB) { /* volume meter – ignored */ }

            @Override
            public void onBufferReceived(byte[] buffer) { /* raw audio – ignored */ }

            @Override
            public void onEndOfSpeech() {
                isListening = false;
                setMicActive(false);
            }

            @Override
            public void onError(int error) {
                isListening = false;
                setMicActive(false);
                String msg;
                switch (error) {
                    case SpeechRecognizer.ERROR_NO_MATCH:
                        msg = "Parole non reconnue. Réessayez.";
                        break;
                    case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                        msg = "Délai dépassé. Appuyez à nouveau sur 🎙️";
                        break;
                    case SpeechRecognizer.ERROR_NETWORK:
                    case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                        msg = "Erreur réseau pour la reconnaissance vocale.";
                        break;
                    case SpeechRecognizer.ERROR_AUDIO:
                        msg = "Erreur audio. Vérifiez le microphone.";
                        break;
                    default:
                        msg = "Erreur de reconnaissance vocale.";
                        break;
                }
                runOnUiThread(() ->
                        Toast.makeText(ChatActivity.this, msg, Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResults(Bundle results) {
                isListening = false;
                setMicActive(false);
                ArrayList<String> matches =
                        results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    String spoken = matches.get(0);
                    runOnUiThread(() -> {
                        // Put spoken text into field so user can edit before sending
                        etMessage.setText(spoken);
                        etMessage.setSelection(spoken.length());
                        // Auto-send
                        addMessage(spoken, ChatMessage.TYPE_USER);
                        etMessage.setText("");
                        sendMessageToGemini();
                    });
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                ArrayList<String> partial =
                        partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (partial != null && !partial.isEmpty()) {
                    runOnUiThread(() -> etMessage.setText(partial.get(0)));
                }
            }

            @Override
            public void onEvent(int eventType, Bundle params) { /* not used */ }
        };
    }

    // ────────────────────────────────────────────────────
    //  Runtime permission result
    // ────────────────────────────────────────────────────

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MIC_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startListening();
            } else {
                Toast.makeText(this, "Permission micro refusée", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // ────────────────────────────────────────────────────
    //  Chat helpers
    // ────────────────────────────────────────────────────

    private void addMessage(String text, int type) {
        runOnUiThread(() -> {
            messageList.add(new ChatMessage(text, type));
            chatAdapter.notifyItemInserted(messageList.size() - 1);
            recyclerViewChat.scrollToPosition(messageList.size() - 1);
        });
    }

    // ────────────────────────────────────────────────────
    //  Gemini API
    // ────────────────────────────────────────────────────

    private void sendMessageToGemini() {
        JsonObject jsonBody = new JsonObject();
        JsonArray contents = new JsonArray();

        for (ChatMessage msg : messageList) {
            JsonObject content = new JsonObject();
            content.addProperty("role",
                    msg.getSenderType() == ChatMessage.TYPE_USER ? "user" : "model");
            JsonArray parts = new JsonArray();
            JsonObject part = new JsonObject();
            part.addProperty("text", msg.getText());
            parts.add(part);
            content.add("parts", parts);
            contents.add(content);
        }

        // System instruction
        JsonObject systemInstruction = new JsonObject();
        JsonArray sysParts = new JsonArray();
        JsonObject sysPart = new JsonObject();
        sysPart.addProperty("text",
                "Tu es un assistant pédagogique pour une application de quiz en programmation. " +
                "Ton rôle est d'expliquer les concepts de manière simple avec des exemples (Java, Python, C, etc.). " +
                "Sois encourageant, utilise du formatage lisible, et aide l'utilisateur à comprendre.");
        sysParts.add(sysPart);
        systemInstruction.add("parts", sysParts);

        jsonBody.add("systemInstruction", systemInstruction);
        jsonBody.add("contents", contents);

        String cleanApiKey = BuildConfig.GEMINI_API_KEY.replace("\"", "").trim();
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent?key=" + cleanApiKey;

        RequestBody body = RequestBody.create(
                jsonBody.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder().url(url).post(body).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("GeminiAPI", "Failed to connect", e);
                addMessage("Erreur de connexion à l'assistant. Veuillez vérifier votre connexion.",
                        ChatMessage.TYPE_BOT);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
                        JsonArray candidates = jsonObject.getAsJsonArray("candidates");
                        if (candidates != null && candidates.size() > 0) {
                            JsonObject content = candidates.get(0).getAsJsonObject()
                                    .getAsJsonObject("content");
                            JsonArray parts = content.getAsJsonArray("parts");
                            if (parts != null && parts.size() > 0) {
                                String botReply = parts.get(0).getAsJsonObject()
                                        .get("text").getAsString();
                                addMessage(botReply, ChatMessage.TYPE_BOT);
                            }
                        }
                    } catch (Exception e) {
                        Log.e("GeminiAPI", "Parsing error", e);
                        addMessage("Erreur de traitement de la réponse.", ChatMessage.TYPE_BOT);
                    }
                } else {
                    String errorBody = "";
                    try {
                        if (response.body() != null) errorBody = response.body().string();
                    } catch (Exception ignored) {}
                    Log.e("GeminiAPI", "Error HTTP " + response.code() + ": " + errorBody);
                    if (response.code() == 429) {
                        addMessage("Quota dépassé. Veuillez réessayer plus tard.", ChatMessage.TYPE_BOT);
                    } else if (response.code() == 401 || response.code() == 403) {
                        addMessage("Erreur d'authentification (Clé API invalide).", ChatMessage.TYPE_BOT);
                    } else {
                        addMessage("Une erreur s'est produite (Code: " + response.code() + ").",
                                ChatMessage.TYPE_BOT);
                    }
                }
            }
        });
    }

    // ────────────────────────────────────────────────────
    //  Lifecycle
    // ────────────────────────────────────────────────────

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
            speechRecognizer = null;
        }
    }
}
