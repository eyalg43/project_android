package com.example.project_android;

import android.content.Intent;
import android.database.CursorWindow;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.project_android.adapters.VideosListAdapter;
import com.example.project_android.entities.User;
import com.example.project_android.entities.VideoData;
import com.example.project_android.viewmodels.VideoViewModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private VideosListAdapter adapter;
    private List<VideoData> allVideos;
    private DrawerLayout drawerLayout;
    private Button toggleModeButton;
    private ImageView toggleModeIcon;
    private Button signInButton;
    private Button signUpButton;
    private ImageView profileImage;
    private TextView welcomeMessage;
    private LinearLayout profileContainer;
    private LinearLayout authButtonsContainer;
    private Button signOutButton;
    private Button uploadVideoButton;
    private VideoViewModel videoViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        RecyclerView listVideos = findViewById(R.id.listVideos);
        adapter = new VideosListAdapter(this, new VideosListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(VideoData video) {
                Intent intent = new Intent(HomePage.this, VideoScreenActivity.class);
                intent.putExtra("video_id", video.getId());
                Log.d("HomePage", "Selected video ID: " + video.getId());
                startActivity(intent);
            }


            @Override
            public void onEditClick(VideoData video) {
                Intent intent = new Intent(HomePage.this, EditVideo.class);
                intent.putExtra("video_id", video.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(VideoData video) {
                // Handle delete
            }
        });
        listVideos.setAdapter(adapter);
        listVideos.setLayoutManager(new LinearLayoutManager(this));

        videoViewModel = new ViewModelProvider(this).get(VideoViewModel.class);
        videoViewModel.getAllVideos().observe(this, videos -> {
            if (videos != null) {
                adapter.setVideos(videos);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoViewModel.fetchVideosFromServer(); // Ensure the latest data is fetched when the activity resumes
    }

    private void filterVideos(String text) {
        List<VideoData> filteredList = new ArrayList<>();
        for (VideoData video : allVideos) {
            if (video.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(video);
            }
        }
        adapter.setVideos(filteredList);
    }



    private void updateVideos() {
        if (allVideos != null) {
            for (VideoData video : allVideos) {
                Log.d("HomePage", "Video Author Image URI: " + video.getAuthorImage());
            }
            adapter.setVideos(allVideos);
        } else {
            Log.e("HomePage", "Error getting videos");
        }
    }

    private void updateModeButtonText() {
        int nightMode = AppCompatDelegate.getDefaultNightMode();
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            toggleModeIcon.setImageResource(R.drawable.ic_light_mode);
        } else {
            toggleModeIcon.setImageResource(R.drawable.ic_dark_mode);
        }
    }

    private void loadImageFromLocalPath(String path, ImageView imageView) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.e("HomePage", "Error loading image: " + e.getMessage());
        }
    }
}
