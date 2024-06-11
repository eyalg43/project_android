package com.example.project_android;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_android.adapters.CommentsAdapter;
import com.example.project_android.adapters.VideoAdapter;
import com.example.project_android.entities.CommentData;
import com.example.project_android.entities.VideoComments;
import com.example.project_android.entities.VideoData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VideoScreenActivity extends AppCompatActivity {

    private static final String TAG = "VideoScreenActivity";

    private RecyclerView relatedVideosRecyclerView;
    private VideoAdapter videoAdapter;
    private List<VideoComments> videoCommentsList;
    private CommentsAdapter commentsAdapter;
    private VideoData currentVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_screen);

        // Load and parse the JSON files
        List<VideoData> videoDataList = loadVideoJSONFromRaw();
        videoCommentsList = loadCommentsJSONFromRaw();

        // Initialize comments RecyclerView with an empty list
        RecyclerView commentsRecyclerView = findViewById(R.id.comments_recycler_view);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsAdapter = new CommentsAdapter(Collections.emptyList());
        commentsRecyclerView.setAdapter(commentsAdapter);

        if (videoDataList != null && !videoDataList.isEmpty()) {
            // Display the first video's details
            VideoData firstVideo = videoDataList.get(0);
            displayVideoDetails(firstVideo);

            // Setup related videos RecyclerView
            relatedVideosRecyclerView = findViewById(R.id.related_videos_recycler_view);
            relatedVideosRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Initialize adapter and set it to RecyclerView
            videoAdapter = new VideoAdapter(videoDataList, this::playSelectedVideo);
            relatedVideosRecyclerView.setAdapter(videoAdapter);


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
            Button submitCommentButton = findViewById(R.id.submit_comment_button);
            EditText usernameInput = findViewById(R.id.username_input);
            EditText commentInput = findViewById(R.id.comment_input);

            submitCommentButton.setOnClickListener(v -> {
                String commentText = commentInput.getText().toString().trim();
                String username = usernameInput.getText().toString().trim();
                if (!commentText.isEmpty() && !username.isEmpty()) {
                    addComment(username, commentText);
                    commentInput.setText("");
                }
            });
        }
    }

    private void addComment(String username, String commentText) {
        if (currentVideo != null) {
            // Assuming static profile picture for now
            CommentData newComment = new CommentData();
            newComment.setId(generateCommentId());
            newComment.setText(commentText);
            newComment.setUsername(username);
            newComment.setDate(getCurrentTime());
            newComment.setImg("img1");

            VideoComments videoComments = findCommentsForVideo(currentVideo.getId());
            if (videoComments != null) {
                videoComments.getComments().add(newComment);
                commentsAdapter.updateComments(videoComments.getComments());
            } else {
                VideoComments newVideoComments = new VideoComments();
                newVideoComments.setVideoId(String.valueOf(currentVideo.getId()));
                newVideoComments.setComments(Collections.singletonList(newComment));
                videoCommentsList.add(newVideoComments);
                commentsAdapter.updateComments(newVideoComments.getComments());
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
        viewsTextView.setText(video.getViews());
        uploadTimeTextView.setText(video.getUploadTime());
        descriptionTextView.setText(video.getDescription());
        authorTextView.setText(video.getAuthor());

        // Load author image
        int imageResource = getResources().getIdentifier(video.getAuthorImage(), "drawable", getPackageName());
        authorImageView.setImageResource(imageResource);

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
                commentsAdapter.updateComments(commentsForVideo.getComments());
            } else {
                commentsAdapter.updateComments(Collections.emptyList());
            }
        } else {
            Log.e("VideoScreenActivity", "commentsAdapter is null when trying to update comments.");
        }
    }

    private List<VideoData> loadVideoJSONFromRaw() {
        InputStream inputStream = getResources().openRawResource(R.raw.videos);
        InputStreamReader reader = new InputStreamReader(inputStream);
        Type listType = new TypeToken<List<VideoData>>() {}.getType();
        return new Gson().fromJson(reader, listType);
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
}
