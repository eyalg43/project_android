package com.example.project_android;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
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
        List<VideoData> videoDataList = loadJSONFromRaw();

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
        }
    }

    private List<VideoData> loadJSONFromRaw() {
        InputStream inputStream = getResources().openRawResource(R.raw.videos);
        InputStreamReader reader = new InputStreamReader(inputStream);
        Type listType = new TypeToken<List<VideoData>>(){}.getType();
        return new Gson().fromJson(reader, listType);
    }
}
