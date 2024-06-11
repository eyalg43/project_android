package com.example.project_android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_android.R;
import com.example.project_android.entities.CommentData;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {
    private List<CommentData> commentsList;

    public CommentsAdapter(List<CommentData> commentsList) {
        // Ensure the commentsList is modifiable
        this.commentsList = new ArrayList<>(commentsList);
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentData comment = commentsList.get(position);
        holder.usernameTextView.setText(comment.getUsername());
        holder.dateTextView.setText(comment.getDate());
        holder.commentTextView.setText(comment.getText());

        // Load user image
        int imageResource = holder.itemView.getContext().getResources().getIdentifier(comment.getImg(), "drawable", holder.itemView.getContext().getPackageName());
        holder.userImageView.setImageResource(imageResource);
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public void updateComments(List<CommentData> newComments) {
        // Ensure the newComments list is modifiable
        commentsList.clear();
        commentsList.addAll(new ArrayList<>(newComments));
        notifyDataSetChanged();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView dateTextView;
        TextView commentTextView;
        ImageView userImageView;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.comment_username);
            dateTextView = itemView.findViewById(R.id.comment_date);
            commentTextView = itemView.findViewById(R.id.comment_text);
            userImageView = itemView.findViewById(R.id.comment_user_image);
        }
    }
}
