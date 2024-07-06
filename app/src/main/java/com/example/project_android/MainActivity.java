package com.example.project_android;

import android.content.Intent;
import android.database.CursorWindow;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_android.entities.CommentData;
import com.example.project_android.entities.VideoData;
import com.example.project_android.viewmodels.CommentViewModel;
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

    private VideoViewModel videoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoViewModel = new ViewModelProvider(this).get(VideoViewModel.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoViewModel.fetchVideosFromServer(); // Fetch videos from the server every time the activity resumes

        Intent intent = new Intent(MainActivity.this, HomePage.class);
        startActivity(intent);
        finish();
    }
}
