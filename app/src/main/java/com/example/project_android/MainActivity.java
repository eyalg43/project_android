package com.example.project_android;

import android.content.Intent;
import android.database.CursorWindow;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.project_android.api.VideoApi;
import com.example.project_android.entities.VideoComments;
import com.example.project_android.entities.VideoData;
import com.example.project_android.viewmodels.VideoViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Increase the CursorWindow size
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
                field.setAccessible(true);
                field.set(null, 100 * 1024 * 1024); // 100MB
            } else {
                Field field = CursorWindow.class.getDeclaredField("mWindowSize");
                field.setAccessible(true);
                field.set(null, 100 * 1024 * 1024); // 100MB
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize CommentState
        CommentState commentState = CommentState.getInstance(this);

        // Load initial comments from JSON if the state is empty
        if (commentState.getAllComments().isEmpty()) {
            List<VideoComments> commentsList = loadCommentsJSONFromRaw();
            for (VideoComments videoComments : commentsList) {
                commentState.addCommentsForVideo(Integer.parseInt(videoComments.getVideoId()), videoComments.getComments());
            }
        }

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

    private List<VideoComments> loadCommentsJSONFromRaw() {
        InputStream inputStream = getResources().openRawResource(R.raw.comments);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        Gson gson = new Gson();
        Type commentsListType = new TypeToken<List<VideoComments>>() {}.getType();
        return gson.fromJson(reader, commentsListType);
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