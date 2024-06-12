package com.example.project_android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_android.adapters.CommentsAdapter;
import com.example.project_android.adapters.VideoAdapter;
import com.example.project_android.entities.CommentData;
import com.example.project_android.entities.VideoData;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VideoScreenActivity extends AppCompatActivity {

    private static final String TAG = "VideoScreenActivity";

    private RecyclerView relatedVideosRecyclerView;
    private VideoAdapter videoAdapter;
    private CommentsAdapter commentsAdapter;
    private VideoData currentVideo;
    private List<VideoData> originalVideoList;
    private NestedScrollView nestedScrollView;
    private CommentState commentState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_screen);

        // Initialize CommentState
        commentState = CommentState.getInstance(this);

        // Load and parse the JSON files
        originalVideoList = VideosState.getInstance().getVideoList();

        // Initialize NestedScrollView
        nestedScrollView = findViewById(R.id.nested_scroll_view);

        // Initialize comments RecyclerView with an empty list
        RecyclerView commentsRecyclerView = findViewById(R.id.comments_recycler_view);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsAdapter = new CommentsAdapter(Collections.emptyList());
        commentsRecyclerView.setAdapter(commentsAdapter);

        // Initialize adapter and set it to RecyclerView
        videoAdapter = new VideoAdapter(originalVideoList, this::playSelectedVideo);
        relatedVideosRecyclerView = findViewById(R.id.related_videos_recycler_view);
        relatedVideosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        relatedVideosRecyclerView.setAdapter(videoAdapter);

        // Initialize the comment input section
        LinearLayout commentInputSection = findViewById(R.id.comment_input_section);
        ImageView userImageInput = findViewById(R.id.user_image_input);
        TextView displayNameTextView = findViewById(R.id.display_name_text_view);
        EditText commentInput = findViewById(R.id.comment_input);
        Button submitCommentButton = findViewById(R.id.submit_comment_button);

        if (UserState.isLoggedIn()) {
            commentInputSection.setVisibility(View.VISIBLE);
            User loggedInUser = UserState.getLoggedInUser();
            displayNameTextView.setText(loggedInUser.getDisplayName());

            // Load user image
            Uri imageUri = Uri.parse(loggedInUser.getImageUri());
            userImageInput.setImageURI(imageUri);
        } else {
            commentInputSection.setVisibility(View.GONE);
        }

        // Get the video ID from the intent
        int videoId = getIntent().getIntExtra("video_id", -1);
        if (videoId != -1) {
            VideoData selectedVideo = findVideoById(videoId);
            if (selectedVideo != null) {
                displayVideoDetails(selectedVideo);
            }
        }

        // Set up the show comments button
        Button showCommentsButton = findViewById(R.id.show_comments_button);
        View commentsContainer = findViewById(R.id.comments_container);
        Button hideCommentsButton = findViewById(R.id.close_comments_button);

        showCommentsButton.setOnClickListener(v -> {
            findViewById(R.id.content_container).setVisibility(View.GONE);
            commentsContainer.setVisibility(View.VISIBLE);
        });

        hideCommentsButton.setOnClickListener(v -> {
            findViewById(R.id.content_container).setVisibility(View.VISIBLE);
            commentsContainer.setVisibility(View.GONE);
        });

        // Handle comment submission
        submitCommentButton.setOnClickListener(v -> {
            if (UserState.isLoggedIn()) {
                String commentText = commentInput.getText().toString().trim();
                String displayName = UserState.getLoggedInUser().getDisplayName();
                String userImage = UserState.getLoggedInUser().getImageUri();
                if (!commentText.isEmpty()) {
                    addComment(displayName, commentText, userImage);
                    commentInput.setText("");
                }
            } else {
                Toast.makeText(VideoScreenActivity.this, "You need to be logged in to comment.", Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize like and dislike buttons
        ImageButton likeButton = findViewById(R.id.like_button);
        ImageButton dislikeButton = findViewById(R.id.dislike_button);

        likeButton.setOnClickListener(v -> handleLikeDislike(true));
        dislikeButton.setOnClickListener(v -> handleLikeDislike(false));
    }

    private void handleLikeDislike(boolean isLike) {
        if (currentVideo != null) {
            if (isLike) {
                if (currentVideo.isLiked()) {
                    currentVideo.setLiked(false);
                } else {
                    currentVideo.setLiked(true);
                    currentVideo.setDisliked(false);
                }
            } else {
                if (currentVideo.isDisliked()) {
                    currentVideo.setDisliked(false);
                } else {
                    currentVideo.setDisliked(true);
                    currentVideo.setLiked(false);
                }
            }

            // Update the video state
            VideosState.getInstance().updateVideo(currentVideo);

            // Update button colors
            updateLikeDislikeButtonColors();
        }
    }

    private void updateLikeDislikeButtonColors() {
        ImageButton likeButton = findViewById(R.id.like_button);
        ImageButton dislikeButton = findViewById(R.id.dislike_button);

        if (currentVideo.isLiked()) {
            likeButton.setColorFilter(getResources().getColor(R.color.like_green));
        } else {
            likeButton.setColorFilter(null);
        }

        if (currentVideo.isDisliked()) {
            dislikeButton.setColorFilter(getResources().getColor(R.color.dislike_red));
        } else {
            dislikeButton.setColorFilter(null);
        }
    }

    private void addComment(String displayName, String commentText, String userImage) {
        if (currentVideo != null) {
            CommentData newComment = new CommentData();
            newComment.setId(generateCommentId());
            newComment.setText(commentText);
            newComment.setUsername(displayName);
            newComment.setDate(getCurrentTime());
            newComment.setImg(userImage);

            List<CommentData> comments = commentState.getCommentsForVideo(currentVideo.getId());
            if (comments == null) {
                comments = new ArrayList<>();
                commentState.addCommentsForVideo(currentVideo.getId(), comments);
            }
            comments.add(newComment);
            commentState.updateCommentsForVideo(currentVideo.getId(), comments);

            commentsAdapter.updateComments(reverseComments(comments));
        }
    }

    private void displayVideoDetails(VideoData video) {
        currentVideo = video;
        TextView titleTextView = findViewById(R.id.video_title);
        TextView viewsTextView = findViewById(R.id.video_views);
        TextView uploadTimeTextView = findViewById(R.id.video_uploadtime);
        TextView descriptionTextView = findViewById(R.id.video_description);
        TextView authorTextView = findViewById(R.id.author_name);
        ImageView authorImageView = findViewById(R.id.author_image);
        VideoView videoView = findViewById(R.id.video_view);

        titleTextView.setText(video.getTitle());
        viewsTextView.setText(video.getViews() + " views");
        uploadTimeTextView.setText(video.getUploadTime());
        descriptionTextView.setText(video.getDescription());
        authorTextView.setText(video.getAuthor());

        // Load author image
        loadImage(video.getAuthorImage(), authorImageView);

        // Load video
        Uri videoUri = Uri.parse(video.getVideo());
        videoView.setVideoURI(videoUri);

        // Add media controls to the VideoView
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.setOnPreparedListener(mp -> {
            mediaController.setAnchorView(videoView);
            videoView.start();
        });

        videoView.start();

        // Load and display comments
        List<CommentData> commentsForVideo = commentState.getCommentsForVideo(video.getId());
        if (commentsForVideo != null) {
            commentsAdapter.updateComments(reverseComments(commentsForVideo));
        } else {
            commentsAdapter.updateComments(Collections.emptyList());
        }

        // Update related videos
        updateRelatedVideos(video);

        // Update the like and dislike button colors
        updateLikeDislikeButtonColors();
    }

    private void loadImage(String path, ImageView imageView) {
        try {
            // Check if the path is a drawable resource
            int resId = getResources().getIdentifier(path, "drawable", getPackageName());
            if (resId != 0) {
                imageView.setImageResource(resId);
                Log.d(TAG, "Loaded drawable resource: " + path);
            } else if (path.startsWith("content://") || path.startsWith("file://")) {
                // Load from URI
                Uri uri = Uri.parse(path);
                InputStream inputStream = getContentResolver().openInputStream(uri);
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

    private int generateCommentId() {
        // Generate a unique comment ID
        int maxId = 0;
        List<CommentData> allComments = new ArrayList<>();
        for (List<CommentData> comments : commentState.getAllComments().values()) {
            allComments.addAll(comments);
        }
        for (CommentData comment : allComments) {
            if (comment.getId() > maxId) {
                maxId = comment.getId();
            }
        }
        return maxId + 1;
    }

    private String getCurrentTime() {
        // Get the current time in "x hours ago" format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        return sdf.format(new Date());
    }

    private VideoData findVideoById(int id) {
        for (VideoData video : originalVideoList) {
            if (video.getId() == id) {
                return video;
            }
        }
        return null;
    }

    private List<CommentData> reverseComments(List<CommentData> comments) {
        List<CommentData> reversedComments = new ArrayList<>(comments);
        Collections.reverse(reversedComments);
        return reversedComments;
    }

    private void playSelectedVideo(VideoData selectedVideo) {
        displayVideoDetails(selectedVideo);
        resetScrollPosition();
    }

    private void updateRelatedVideos(VideoData currentVideo) {
        if (videoAdapter != null) {
            List<VideoData> filteredVideos = new ArrayList<>();

            for (VideoData video : originalVideoList) {
                if (video.getId() != currentVideo.getId()) {
                    filteredVideos.add(video);
                }
            }

            videoAdapter.updateVideoList(filteredVideos);
        } else {
            Log.e(TAG, "videoAdapter is null when trying to update related videos.");
        }
    }

    private void resetScrollPosition() {
        // Reset the scroll position to the top
        nestedScrollView.scrollTo(0, 0);
    }
}
