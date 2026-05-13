package com.example.programmingquiz_bidaoui.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.programmingquiz_bidaoui.R;
import com.example.programmingquiz_bidaoui.models.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        
        holder.tvRank.setText(String.valueOf(position + 1));
        holder.tvName.setText(user.getName());
        holder.tvScore.setText(String.valueOf(user.getTotalScore()));

        String level = user.getUserLevel() != null ? user.getUserLevel() : "Beginner";
        int streak = user.getCurrentStreak();
        holder.tvLevelInfo.setText(level + " • 🔥 " + streak + " Day Streak");
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvRank, tvName, tvLevelInfo, tvScore;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.tvRank);
            tvName = itemView.findViewById(R.id.tvName);
            tvLevelInfo = itemView.findViewById(R.id.tvLevelInfo);
            tvScore = itemView.findViewById(R.id.tvScore);
        }
    }
}
