package com.example.project_android;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

public class VideoScreenActivity extends AppCompatActivity {

    private RecyclerView relatedVideosRecyclerView;
    private VideoAdapter videoAdapter;
    private List<VideoData> relatedVideosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_screen);

        // Load and parse the JSON file
        List<VideoData> videoDataList = loadVideoJSONFromRaw();
        List<VideoComments> videoCommentsList = loadCommentsJSONFromRaw();

        if (videoDataList != null && !videoDataList.isEmpty()) {
            // Display the first video's details
            VideoData firstVideo = videoDataList.get(0);

            TextView titleTextView = findViewById(R.id.video_title);
            TextView viewsTextView = findViewById(R.id.video_views);
            TextView uploadTimeTextView = findViewById(R.id.video_uploadtime);
            TextView descriptionTextView = findViewById(R.id.video_description);
            TextView authorTextView = findViewById(R.id.author_name);
            ImageView authorImageView = findViewById(R.id.author_image);
            VideoView videoView = findViewById(R.id.video_view);

            titleTextView.setText(firstVideo.getTitle());
            viewsTextView.setText(firstVideo.getViews());
            uploadTimeTextView.setText(firstVideo.getUploadTime());
            descriptionTextView.setText(firstVideo.getDescription());
            authorTextView.setText(firstVideo.getAuthor());

            // Load author image
            int imageResource = getResources().getIdentifier(firstVideo.getAuthorImage(), "drawable", getPackageName());
            authorImageView.setImageResource(imageResource);

            // Load video
            Uri videoUri = Uri.parse(firstVideo.getVideo());
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

            // Setup related videos RecyclerView
            relatedVideosRecyclerView = findViewById(R.id.related_videos_recycler_view);
            relatedVideosRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Remove the first video from the list to display related videos
            videoDataList.remove(0);

            // Initialize adapter and set it to RecyclerView
            videoAdapter = new VideoAdapter(videoDataList, this::playSelectedVideo);
            relatedVideosRecyclerView.setAdapter(videoAdapter);

            // Display comments
            VideoComments commentsForFirstVideo = findCommentsForVideo(videoCommentsList, firstVideo.getId());
            if (commentsForFirstVideo != null) {
                RecyclerView commentsRecyclerView = findViewById(R.id.comments_recycler_view);
                commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                CommentsAdapter commentsAdapter = new CommentsAdapter(commentsForFirstVideo.getComments());
                commentsRecyclerView.setAdapter(commentsAdapter);
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

    private VideoComments findCommentsForVideo(List<VideoComments> videoCommentsList, int videoId) {
        String videoIdStr = String.valueOf(videoId);
        for (VideoComments videoComments : videoCommentsList) {
            if (videoComments.getVideoId().equals(videoIdStr)) {
                return videoComments;
            }
        }
        return null;
    }

    private void playSelectedVideo(VideoData selectedVideo) {
        TextView titleTextView = findViewById(R.id.video_title);
        TextView viewsTextView = findViewById(R.id.video_views);
        TextView uploadTimeTextView = findViewById(R.id.video_uploadtime);
        TextView descriptionTextView = findViewById(R.id.video_description);
        TextView authorTextView = findViewById(R.id.author_name);
        ImageView authorImageView = findViewById(R.id.author_image);
        VideoView videoView = findViewById(R.id.video_view);

        titleTextView.setText(selectedVideo.getTitle());
        viewsTextView.setText(selectedVideo.getViews());
        uploadTimeTextView.setText(selectedVideo.getUploadTime());
        descriptionTextView.setText(selectedVideo.getDescription());
        authorTextView.setText(selectedVideo.getAuthor());

        // Load author image
        int imageResource = getResources().getIdentifier(selectedVideo.getAuthorImage(), "drawable", getPackageName());
        authorImageView.setImageResource(imageResource);

        // Load video
        Uri videoUri = Uri.parse(selectedVideo.getVideo());
        videoView.setVideoURI(videoUri);

        videoView.setOnPreparedListener(mp -> videoView.start());
    }
}
