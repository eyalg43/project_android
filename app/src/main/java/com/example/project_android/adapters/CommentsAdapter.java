package com.example.project_android.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_android.R;
import com.example.project_android.UserState;
import com.example.project_android.entities.CommentData;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {
    private List<CommentData> commentsList;
    private Context context;

    public CommentsAdapter(List<CommentData> commentsList) {
        // Ensure the commentsList is modifiable
        this.commentsList = new ArrayList<>(commentsList);
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentData comment = commentsList.get(position);
        holder.usernameTextView.setText(comment.getUsername());
        holder.dateTextView.setText(comment.getDate());
        holder.commentTextView.setText(comment.getText());

        // Load user image
        String imgUri = comment.getImg();
        if (imgUri != null && imgUri.startsWith("content://")) {
            // If img is a URI, load it as such
            holder.userImageView.setImageURI(Uri.parse(imgUri));
        } else {
            // Otherwise, treat it as a drawable resource
            int imageResource = context.getResources().getIdentifier(comment.getImg(), "drawable", context.getPackageName());
            holder.userImageView.setImageResource(imageResource);
        }

        // Show/hide edit and delete buttons based on login status and comment ownership
        if (UserState.isLoggedIn() && UserState.getLoggedInUser().getDisplayName().equals(comment.getUsername())) {
            holder.editButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.VISIBLE);
        } else {
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        }

        holder.editButton.setOnClickListener(v -> {
            showEditCommentDialog(comment);
        });

        holder.deleteButton.setOnClickListener(v -> {
            deleteComment(comment);
        });
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

    private void showEditCommentDialog(CommentData commentData) {
        // Show a dialog to edit the comment with a custom theme
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
        builder.setTitle("Edit Comment");

        // Set up the input
        final EditText input = new EditText(context);
        input.setText(commentData.getText());
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Save", (dialog, which) -> {
            String newCommentText = input.getText().toString().trim();
            if (!newCommentText.isEmpty()) {
                commentData.setText(newCommentText);
                notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }


    private void deleteComment(CommentData commentData) {
        commentsList.remove(commentData);
        notifyDataSetChanged();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView dateTextView;
        TextView commentTextView;
        ImageView userImageView;
        Button editButton;
        Button deleteButton;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.comment_username);
            dateTextView = itemView.findViewById(R.id.comment_date);
            commentTextView = itemView.findViewById(R.id.comment_text);
            userImageView = itemView.findViewById(R.id.comment_user_image);
            editButton = itemView.findViewById(R.id.edit_comment_button);
            deleteButton = itemView.findViewById(R.id.delete_comment_button);
        }
    }
}
