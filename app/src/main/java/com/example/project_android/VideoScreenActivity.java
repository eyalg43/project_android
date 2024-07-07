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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_android.adapters.CommentsAdapter;
import com.example.project_android.adapters.VideoAdapter;
import com.example.project_android.entities.CommentData;
import com.example.project_android.entities.User;
import com.example.project_android.entities.VideoData;
import com.example.project_android.viewmodels.CommentViewModel;
import com.example.project_android.viewmodels.VideoViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VideoScreenActivity extends AppCompatActivity {

    private static final String TAG = "VideoScreenActivity";

    private RecyclerView relatedVideosRecyclerView;
    private VideoAdapter videoAdapter;
    private CommentsAdapter commentsAdapter;
    private VideoData currentVideo;
    private List<VideoData> originalVideoList;
    private NestedScrollView nestedScrollView;
    private VideoViewModel videoViewModel;

    private CommentViewModel commentViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_screen);

        // Initialize VideoViewModel
        videoViewModel = new ViewModelProvider(this).get(VideoViewModel.class);

        // Initialize CommentViewModel
        commentViewModel = new ViewModelProvider(this).get(CommentViewModel.class);

        // Initialize NestedScrollView
        nestedScrollView = findViewById(R.id.nested_scroll_view);

        // Initialize comments RecyclerView with an empty list
        RecyclerView commentsRecyclerView = findViewById(R.id.comments_recycler_view);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsAdapter = new CommentsAdapter(this, Collections.emptyList());
        commentsRecyclerView.setAdapter(commentsAdapter);

        // Initialize adapter and set it to RecyclerView
        videoAdapter = new VideoAdapter(new ArrayList<>(), this::playSelectedVideo);
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
        String videoId = getIntent().getStringExtra("video_id");
        Log.d(TAG, "Received video ID: " + videoId); // Log received video ID
        if (videoId != null) {
            videoViewModel.getAllVideos().observe(this, new Observer<List<VideoData>>() {
                @Override
                public void onChanged(List<VideoData> videos) {
                    if (videos != null) {
                        originalVideoList = videos;
                        VideoData selectedVideo = findVideoById(videoId);
                        if (selectedVideo != null) {
                            displayVideoDetails(selectedVideo);
                            observeComments(selectedVideo.getId()); // Uncomment if you add comments feature back
                        }
                    }
                }
            });
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

        /*likeButton.setOnClickListener(v -> handleLikeDislike(true));!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        dislikeButton.setOnClickListener(v -> handleLikeDislike(false));!!!!!!!!!!!!!!!!!!!!!!!*/
    }

    /*private void handleLikeDislike(boolean isLike) {
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
    }*/

    /*private void updateLikeDislikeButtonColors() {
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
    }*/

    private void addComment(String displayName, String commentText, String userImage) {
        if (currentVideo != null) {
            CommentData newComment = new CommentData();
            // No need to set the ID manually, MongoDB will handle it
            newComment.setText(commentText);
            newComment.setUsername(UserState.getLoggedInUser().getUsername());
            newComment.setDisplayName(displayName);
            newComment.setDate(getCurrentTime());
            newComment.setImg(userImage);
            newComment.setVideoId(currentVideo.getId());

            commentViewModel.createComment(newComment); // Use ViewModel to handle comment creation
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
        if (video.getVideo().startsWith("data:video")) {
            // Base64 encoded video
            String base64Video = video.getVideo().split(",")[1];
            byte[] decodedBytes = android.util.Base64.decode(base64Video, android.util.Base64.DEFAULT);

            // Create a temporary file to store the decoded video
            try {
                File tempFile = File.createTempFile("video", "mp4", getCacheDir());
                FileOutputStream fos = new FileOutputStream(tempFile);
                fos.write(decodedBytes);
                fos.close();

                Uri videoUri = Uri.fromFile(tempFile);
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
            } catch (IOException e) {
                Log.e(TAG, "Error loading video: " + e.getMessage());
            }
        } else {
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
        }

        // Load and display comments
        observeComments(video.getId());

        // Update related videos
        updateRelatedVideos(video);

        // Update the like and dislike button colors
        //updateLikeDislikeButtonColors();
    }
    private void observeComments(String videoId) {
        commentViewModel.getComments(videoId).observe(this, comments -> {
            if (comments != null) {
                commentsAdapter.updateComments(reverseComments(comments));
            } else {
                commentsAdapter.updateComments(Collections.emptyList());
            }
        });
    }

    private void loadImage(String path, ImageView imageView) {
        try {
            if (path.startsWith("data:image")) {
                // Base64 encoded image
                String base64Image = path.split(",")[1];
                byte[] decodedString = android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageView.setImageBitmap(decodedByte);
                Log.d(TAG, "Loaded base64 image.");
            } else {
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
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading image: " + e.getMessage());
        }
    }

    private String getCurrentTime() {
        return "Just now";
    }

    private VideoData findVideoById(String id) {
        if (originalVideoList != null) {
            for (VideoData video : originalVideoList) {
                if (video.getId() != null && video.getId().equals(id)) {
                    return video;
                }
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
                if (!video.getId().equals(currentVideo.getId())) {
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
