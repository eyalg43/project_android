package com.example.project_android.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_android.CommentState;
import com.example.project_android.R;
import com.example.project_android.UserState;
import com.example.project_android.entities.CommentData;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {
    private List<CommentData> commentsList;
    private Context context;
    private static final String TAG = "CommentsAdapter";

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
        loadImage(comment.getImg(), holder.userImageView);

        // Show/hide edit and delete buttons based on login status and comment ownership
        if (UserState.isLoggedIn() && UserState.getLoggedInUser().getDisplayName().equals(comment.getUsername())) {
            holder.editButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.VISIBLE);
        } else {
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        }

        holder.editButton.setOnClickListener(v -> showEditCommentDialog(comment));
        holder.deleteButton.setOnClickListener(v -> deleteComment(comment));

        holder.likeButton.setOnClickListener(v -> handleLikeDislike(comment, true, holder));
        holder.dislikeButton.setOnClickListener(v -> handleLikeDislike(comment, false, holder));

        // Update button colors based on comment state
        updateLikeDislikeButtonColors(comment, holder);
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
                CommentState.getInstance().updateCommentData(commentData);
                notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void deleteComment(CommentData commentData) {
        commentsList.remove(commentData);
        CommentState.getInstance().getCommentData(commentData.getId()).setLiked(false);
        CommentState.getInstance().getCommentData(commentData.getId()).setDisliked(false);
        CommentState.getInstance().updateCommentData(commentData);
        notifyDataSetChanged();
    }

    private void handleLikeDislike(CommentData commentData, boolean isLike, CommentViewHolder holder) {
        if (isLike) {
            if (commentData.isLiked()) {
                commentData.setLiked(false);
            } else {
                commentData.setLiked(true);
                commentData.setDisliked(false);
            }
        } else {
            if (commentData.isDisliked()) {
                commentData.setDisliked(false);
            } else {
                commentData.setDisliked(true);
                commentData.setLiked(false);
            }
        }

        CommentState.getInstance().updateCommentData(commentData);
        updateLikeDislikeButtonColors(commentData, holder);
    }

    private void updateLikeDislikeButtonColors(CommentData commentData, CommentViewHolder holder) {
        if (commentData.isLiked()) {
            holder.likeButton.setColorFilter(context.getResources().getColor(R.color.like_green));
        } else {
            holder.likeButton.setColorFilter(null);
        }

        if (commentData.isDisliked()) {
            holder.dislikeButton.setColorFilter(context.getResources().getColor(R.color.dislike_red));
        } else {
            holder.dislikeButton.setColorFilter(null);
        }
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView dateTextView;
        TextView commentTextView;
        ImageView userImageView;
        Button editButton;
        Button deleteButton;
        ImageButton likeButton;
        ImageButton dislikeButton;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.comment_username);
            dateTextView = itemView.findViewById(R.id.comment_date);
            commentTextView = itemView.findViewById(R.id.comment_text);
            userImageView = itemView.findViewById(R.id.comment_user_image);
            editButton = itemView.findViewById(R.id.edit_comment_button);
            deleteButton = itemView.findViewById(R.id.delete_comment_button);
            likeButton = itemView.findViewById(R.id.like_comment_button);
            dislikeButton = itemView.findViewById(R.id.dislike_comment_button);
        }
    }

    private void loadImage(String path, ImageView imageView) {
        try {
            // Check if the path is a drawable resource
            int resId = imageView.getContext().getResources().getIdentifier(path, "drawable", imageView.getContext().getPackageName());
            if (resId != 0) {
                imageView.setImageResource(resId);
                Log.d(TAG, "Loaded drawable resource: " + path);
            } else if (path.startsWith("content://") || path.startsWith("file://")) {
                // Load from URI
                Uri uri = Uri.parse(path);
                InputStream inputStream = imageView.getContext().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
                if (inputStream != null) {
                    inputStream.close();
                }
                Log.d(TAG, "Loaded image from URI: " + path);
            } else {
                // Load from local file path
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                imageView.setImageBitmap(bitmap);
                Log.d(TAG, "Loaded image from local file path: " + path);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading image: " + e.getMessage());
        }
    }
}
