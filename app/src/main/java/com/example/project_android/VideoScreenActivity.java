package com.example.project_android;

import android.net.Uri;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_screen);

        // Load and parse the JSON file
        List<VideoData> videoDataList = loadJSONFromRaw(R.raw.videos, new TypeToken<List<VideoData>>(){}.getType());
        List<VideoComments> videoCommentsList = loadJSONFromRaw(R.raw.comments, new TypeToken<List<VideoComments>>(){}.getType());

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
            uploadTimeTextView.setText(firstVideo.getUploadtime());
            descriptionTextView.setText(firstVideo.getDescription());
            authorTextView.setText(firstVideo.getAuthor());

            // Load author image
            int imageResource = getResources().getIdentifier(firstVideo.getAuthorImage(), "drawable", getPackageName());
            authorImageView.setImageResource(imageResource);

            // Load video
            Uri videoUri = Uri.parse(firstVideo.getUrl());
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

            // Display comments
            VideoComments commentsForFirstVideo = findCommentsForVideo(videoCommentsList, firstVideo.getId());
            if (commentsForFirstVideo != null) {
                RecyclerView commentsRecyclerView = findViewById(R.id.comments_recycler_view);
                commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                CommentsAdapter commentsAdapter = new CommentsAdapter(commentsForFirstVideo.getComments());
                commentsRecyclerView.setAdapter(commentsAdapter);
            }
        }
    }

    private <T> T loadJSONFromRaw(int resourceId, Type type) {
        InputStream inputStream = getResources().openRawResource(resourceId);
        InputStreamReader reader = new InputStreamReader(inputStream);
        return new Gson().fromJson(reader, type);
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
}
