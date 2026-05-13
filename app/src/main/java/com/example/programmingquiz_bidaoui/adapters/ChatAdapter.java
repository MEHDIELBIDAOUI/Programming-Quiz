package com.example.programmingquiz_bidaoui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.programmingquiz_bidaoui.R;
import com.example.programmingquiz_bidaoui.models.ChatMessage;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ChatMessage> messageList;

    public ChatAdapter(List<ChatMessage> messageList) {
        this.messageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).getSenderType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ChatMessage.TYPE_USER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_user, parent, false);
            return new UserMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_bot, parent, false);
            return new BotMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);
        if (holder.getItemViewType() == ChatMessage.TYPE_USER) {
            ((UserMessageViewHolder) holder).tvUserMessage.setText(message.getText());
        } else {
            ((BotMessageViewHolder) holder).tvBotMessage.setText(message.getText());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class UserMessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserMessage;

        public UserMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserMessage = itemView.findViewById(R.id.tvUserMessage);
        }
    }

    static class BotMessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvBotMessage;

        public BotMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBotMessage = itemView.findViewById(R.id.tvBotMessage);
        }
    }
}
