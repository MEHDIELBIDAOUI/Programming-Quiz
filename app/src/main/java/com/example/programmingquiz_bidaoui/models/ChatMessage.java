package com.example.programmingquiz_bidaoui.models;

public class ChatMessage {
    public static final int TYPE_USER = 0;
    public static final int TYPE_BOT = 1;

    private String text;
    private int senderType;

    public ChatMessage(String text, int senderType) {
        this.text = text;
        this.senderType = senderType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getSenderType() {
        return senderType;
    }

    public void setSenderType(int senderType) {
        this.senderType = senderType;
    }
}
