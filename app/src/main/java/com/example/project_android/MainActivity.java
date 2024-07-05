package com.example.project_android;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_android.entities.CommentData;
import com.example.project_android.entities.VideoComments;
import com.example.project_android.entities.VideoData;
import com.example.project_android.viewmodels.CommentViewModel;
import com.example.project_android.viewmodels.VideoViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private VideoViewModel videoViewModel;
    private CommentViewModel commentViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize ViewModels
        videoViewModel = new ViewModelProvider(this).get(VideoViewModel.class);
        commentViewModel = new ViewModelProvider(this).get(CommentViewModel.class);

        // Load videos from server
        videoViewModel.getAllVideos().observe(this, new Observer<List<VideoData>>() {
            @Override
            public void onChanged(List<VideoData> videoList) {
                if (videoList != null && !videoList.isEmpty()) {
                    VideosState.getInstance().setVideoList(videoList);
                } else {
                    // Fallback to load videos from JSON if server data is not available
                    List<VideoData> localVideoList = loadVideosFromJson();
                    VideosState.getInstance().setVideoList(localVideoList);
                }

                // Load comments for each video from server
                for (VideoData video : VideosState.getInstance().getVideoList()) {
                    String videoId = video.getId();
                    commentViewModel.getComments(videoId).observe(MainActivity.this, new Observer<List<CommentData>>() {
                        @Override
                        public void onChanged(List<CommentData> comments) {
                            if (comments != null) {
                                // Assuming CommentRepository handles the state internally
                                for (CommentData comment : comments) {
                                    commentViewModel.createComment(comment);
                                }
                            }
                        }
                    });
                }
            }
        });

        // Apply theme based on system settings
        applyTheme();

        // Start HomePage activity
        Intent intent = new Intent(MainActivity.this, HomePage.class);
        startActivity(intent);

        // Finish MainActivity to prevent looping
        finish();
    }

    private List<VideoData> loadVideosFromJson() {
        InputStream inputStream = getResources().openRawResource(R.raw.videos);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        Gson gson = new Gson();
        Type videoListType = new TypeToken<List<VideoData>>() {}.getType();
        return gson.fromJson(reader, videoListType);
    }

    private void applyTheme() {
        if ((getResources().getConfiguration().uiMode &
                android.content.res.Configuration.UI_MODE_NIGHT_MASK) ==
                android.content.res.Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}