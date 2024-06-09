package com.example.project_android;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.project_android.adapters.VideosListAdapter;
import com.example.project_android.entities.VideoData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private VideosListAdapter adapter;
    private List<VideoData> allVideos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        RecyclerView listVideos = findViewById(R.id.listVideos);
        adapter = new VideosListAdapter(this);
        listVideos.setAdapter(adapter);
        listVideos.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout = findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<VideoData> videos = loadVideosFromJson();
                        if (videos != null) {
                            adapter.setVideos(videos);
                        } else {
                            Log.e("HomePage", "Failed to load videos from JSON");
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        allVideos = loadVideosFromJson();
        if (allVideos != null) {
            adapter.setVideos(allVideos);
        } else {
            Log.e("HomePage", "Failed to load videos from JSON");
        }
        swipeRefreshLayout.setRefreshing(false);

        ImageButton searchButton = findViewById(R.id.search_button);
        final SearchView searchView = findViewById(R.id.searchView);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchView.getVisibility() == View.GONE) {
                    searchView.setVisibility(View.VISIBLE);
                } else {
                    searchView.setVisibility(View.GONE);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterVideos(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterVideos(newText);
                return false;
            }
        });
    }

    private List<VideoData> loadVideosFromJson() {
        InputStream inputStream = getResources().openRawResource(R.raw.videos);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        Gson gson = new Gson();
        Type videoListType = new TypeToken<List<VideoData>>() {}.getType();
        return gson.fromJson(reader, videoListType);
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
}