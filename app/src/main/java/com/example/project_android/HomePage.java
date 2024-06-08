package com.example.project_android;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_android.adapters.VideosListAdapter;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        RecyclerView listVideos = findViewById(R.id.listVideos);
        final VideosListAdapter adapter = new VideosListAdapter(this);
        listVideos.setAdapter(adapter);
        listVideos.setLayoutManager(new LinearLayoutManager(this));
    }
}