package com.example.programmingquiz_bidaoui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerViewChat;
    private EditText etMessage;
    private ImageButton btnSend;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> messageList;
    private OkHttpClient client;

    private static final String API_KEY = BuildConfig.GEMINI_API_KEY;
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        MaterialToolbar toolbar = findViewById(R.id.toolbarChat);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);

        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // layoutManager.setStackFromEnd(true);
        recyclerViewChat.setLayoutManager(layoutManager);
        recyclerViewChat.setAdapter(chatAdapter);

        client = new OkHttpClient();

        // Welcome message
        addMessage("Bonjour ! Je suis ton assistant pédagogique en programmation. Comment puis-je t'aider aujourd'hui ?", ChatMessage.TYPE_BOT);

        btnSend.setOnClickListener(v -> {
            String text = etMessage.getText().toString().trim();
            if (!text.isEmpty()) {
                addMessage(text, ChatMessage.TYPE_USER);
                etMessage.setText("");
                sendMessageToGemini();
            }
        });
    }

    private void addMessage(String text, int type) {
        runOnUiThread(() -> {
            messageList.add(new ChatMessage(text, type));
            chatAdapter.notifyItemInserted(messageList.size() - 1);
            recyclerViewChat.scrollToPosition(messageList.size() - 1);
        });
    }

    private void sendMessageToGemini() {
        // Build JSON body
        JsonObject jsonBody = new JsonObject();
        JsonArray contents = new JsonArray();

        for (ChatMessage msg : messageList) {
            JsonObject content = new JsonObject();
            content.addProperty("role", msg.getSenderType() == ChatMessage.TYPE_USER ? "user" : "model");
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
        sysPart.addProperty("text", "Tu es un assistant pédagogique pour une application de quiz en programmation. " +
                "Ton rôle est d'expliquer les concepts de manière simple avec des exemples (Java, Python, C, etc.). " +
                "Sois encourageant, utilise du formatage lisible, et aide l'utilisateur à comprendre.");
        sysParts.add(sysPart);
        systemInstruction.add("parts", sysParts);

        jsonBody.add("systemInstruction", systemInstruction);
        jsonBody.add("contents", contents);

        // Clean API key just in case it has quotes or newlines from Windows
        String cleanApiKey = BuildConfig.GEMINI_API_KEY.replace("\"", "").trim();
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent?key=" + cleanApiKey;

        RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("GeminiAPI", "Failed to connect", e);
                addMessage("Erreur de connexion à l'assistant. Veuillez vérifier votre connexion.", ChatMessage.TYPE_BOT);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
                        JsonArray candidates = jsonObject.getAsJsonArray("candidates");
                        if (candidates != null && candidates.size() > 0) {
                            JsonObject content = candidates.get(0).getAsJsonObject().getAsJsonObject("content");
                            JsonArray parts = content.getAsJsonArray("parts");
                            if (parts != null && parts.size() > 0) {
                                String botReply = parts.get(0).getAsJsonObject().get("text").getAsString();
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
                        if (response.body() != null) {
                            errorBody = response.body().string();
                        }
                    } catch (Exception ignored) {}
                    
                    Log.e("GeminiAPI", "Error HTTP " + response.code() + ": " + errorBody);
                    
                    if (response.code() == 429) {
                        addMessage("Vous avez dépassé le quota d'utilisation de l'assistant (trop de requêtes). Veuillez réessayer plus tard.", ChatMessage.TYPE_BOT);
                    } else if (response.code() == 401 || response.code() == 403) {
                        addMessage("Erreur d'authentification (Clé API invalide).", ChatMessage.TYPE_BOT);
                    } else {
                        addMessage("Une erreur s'est produite avec l'assistant (Code: " + response.code() + ").", ChatMessage.TYPE_BOT);
                    }
                }
            }
        });
    }
}
