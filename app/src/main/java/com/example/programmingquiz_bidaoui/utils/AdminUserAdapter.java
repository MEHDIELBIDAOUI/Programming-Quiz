package com.example.programmingquiz_bidaoui.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.programmingquiz_bidaoui.R;
import com.example.programmingquiz_bidaoui.models.User;

import java.util.List;

public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.UserViewHolder> {

    private List<User> userList;
    private OnUserDeleteListener listener;

    public interface OnUserDeleteListener {
        void onDeleteClick(User user);
    }

    public AdminUserAdapter(List<User> userList, OnUserDeleteListener listener) {
        this.userList = userList;
        this.listener = listener;
    }

    public void setUsers(List<User> newUsers) {
        this.userList = newUsers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User u = userList.get(position);
        holder.tvUserName.setText(u.getName() + (u.isAdmin() ? " (Admin)" : ""));
        holder.tvUserStats.setText("Score: " + u.getTotalScore() + " • Streak: " + u.getCurrentStreak() + " • " + u.getUserLevel());

        holder.ivDeleteUser.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(u);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvUserStats;
        ImageView ivDeleteUser;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserStats = itemView.findViewById(R.id.tvUserStats);
            ivDeleteUser = itemView.findViewById(R.id.ivDeleteUser);
        }
    }
}
