package com.example.programmingquiz_bidaoui.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.programmingquiz_bidaoui.R;
import com.example.programmingquiz_bidaoui.models.Question;

import java.util.List;

public class AdminQuestionAdapter extends RecyclerView.Adapter<AdminQuestionAdapter.QuestionViewHolder> {

    private List<Question> questionList;
    private OnQuestionActionListener listener;

    public interface OnQuestionActionListener {
        void onDeleteClick(Question question);
        void onEditClick(Question question);
        void onPreviewClick(Question question);
    }

    public AdminQuestionAdapter(List<Question> questionList, OnQuestionActionListener listener) {
        this.questionList = questionList;
        this.listener = listener;
    }

    public void setQuestions(List<Question> newQuestions) {
        this.questionList = newQuestions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question q = questionList.get(position);
        holder.tvQuestionTitle.setText(q.getQuestion().split("\n")[0]); // Just first line if code block
        holder.tvCategoryLevel.setText(q.getCategory() + " - " + q.getLevel());
        
        if (q.isCodeChallenge()) {
            holder.tvCodeLabel.setVisibility(View.VISIBLE);
        } else {
            holder.tvCodeLabel.setVisibility(View.GONE);
        }

        holder.ivDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClick(q);
        });

        holder.ivEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEditClick(q);
        });

        holder.ivPreview.setOnClickListener(v -> {
            if (listener != null) listener.onPreviewClick(q);
        });
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionTitle, tvCategoryLevel, tvCodeLabel;
        ImageView ivDelete, ivEdit, ivPreview;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionTitle = itemView.findViewById(R.id.tvQuestionTitle);
            tvCategoryLevel = itemView.findViewById(R.id.tvCategoryLevel);
            tvCodeLabel = itemView.findViewById(R.id.tvCodeLabel);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivPreview = itemView.findViewById(R.id.ivPreview);
        }
    }
}
