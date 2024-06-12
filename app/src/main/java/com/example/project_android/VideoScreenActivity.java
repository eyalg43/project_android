package com.example.project_android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.project_android.entities.VideoComments;
import com.example.project_android.entities.VideoData;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class VideoScreenActivity extends AppCompatActivity {

    private static final String TAG = "VideoScreenActivity";

    private RecyclerView relatedVideosRecyclerView;
    private VideoAdapter videoAdapter;
    private List<VideoComments> videoCommentsList;
    private CommentsAdapter commentsAdapter;
    private VideoData currentVideo;
    private List<VideoData> originalVideoList;
    private NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_screen);

        // Load and parse the JSON files
        originalVideoList = VideosState.getInstance().getVideoList(); // Change videoDataList to originalVideoList
        videoCommentsList = loadCommentsJSONFromRaw();

        // Initialize NestedScrollView
        nestedScrollView = findViewById(R.id.nested_scroll_view);

        // Initialize comments RecyclerView with an empty list
        RecyclerView commentsRecyclerView = findViewById(R.id.comments_recycler_view);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsAdapter = new CommentsAdapter(Collections.emptyList());
        commentsRecyclerView.setAdapter(commentsAdapter);

        // Initialize adapter and set it to RecyclerView
        videoAdapter = new VideoAdapter(originalVideoList, this::playSelectedVideo); // Change videoDataList to originalVideoList
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
    }

    private void addComment(String displayName, String commentText, String userImage) {
        if (currentVideo != null) {
            CommentData newComment = new CommentData();
            newComment.setId(generateCommentId());
            newComment.setText(commentText);
            newComment.setUsername(displayName);
            newComment.setDate(getCurrentTime());
            newComment.setImg(userImage);

            VideoComments videoComments = findCommentsForVideo(currentVideo.getId());
            if (videoComments != null) {
                videoComments.getComments().add(newComment);
                commentsAdapter.updateComments(reverseComments(videoComments.getComments()));
            } else {
                VideoComments newVideoComments = new VideoComments();
                newVideoComments.setVideoId(String.valueOf(currentVideo.getId()));
                newVideoComments.setComments(Collections.singletonList(newComment));
                videoCommentsList.add(newVideoComments);
                commentsAdapter.updateComments(reverseComments(newVideoComments.getComments()));
            }
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
        VideoComments commentsForVideo = findCommentsForVideo(video.getId());
        if (commentsAdapter != null) {
            if (commentsForVideo != null && commentsForVideo.getComments() != null) {
                commentsAdapter.updateComments(reverseComments(commentsForVideo.getComments()));
            } else {
                commentsAdapter.updateComments(Collections.emptyList());
            }
        } else {
            Log.e("VideoScreenActivity", "commentsAdapter is null when trying to update comments.");
        }

        // Update related videos
        updateRelatedVideos(video);
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



    private List<VideoComments> loadCommentsJSONFromRaw() {
        InputStream inputStream = getResources().openRawResource(R.raw.comments);
        InputStreamReader reader = new InputStreamReader(inputStream);
        Type listType = new TypeToken<List<VideoComments>>() {}.getType();
        return new Gson().fromJson(reader, listType);
    }

    private VideoComments findCommentsForVideo(int videoId) {
        String videoIdStr = String.valueOf(videoId);
        for (VideoComments videoComments : videoCommentsList) {
            if (videoComments.getVideoId().equals(videoIdStr)) {
                return videoComments;
            }
        }
        return null;
    }

    private void playSelectedVideo(VideoData selectedVideo) {
        displayVideoDetails(selectedVideo);
        resetScrollPosition();
    }

    private void updateRelatedVideos(VideoData currentVideo) {
        if (videoAdapter != null) {
            List<VideoData> filteredVideos = new ArrayList<>();

            for (VideoData video : originalVideoList) { // Change videoAdapter.getVideoList() to originalVideoList
                if (video.getId() != currentVideo.getId()) {
                    filteredVideos.add(video);
                }
            }

            // Update the adapter with the filtered list
            videoAdapter.updateVideoList(filteredVideos);
        } else {
            Log.e(TAG, "videoAdapter is null when trying to update related videos.");
        }
    }

    private void resetScrollPosition() {
        // Reset the scroll position to the top
        nestedScrollView.scrollTo(0, 0);
    }

    private int generateCommentId() {
        // Generate a unique comment ID
        int maxId = 0;
        for (VideoComments videoComments : videoCommentsList) {
            for (CommentData comment : videoComments.getComments()) {
                if (comment.getId() > maxId) {
                    maxId = comment.getId();
                }
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

}
