package com.example.project_android;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;
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

public class HomePage extends BaseActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private VideosListAdapter adapter;
    private List<VideoData> allVideos;
    private DrawerLayout drawerLayout;
    private Button toggleModeButton;
    private ImageView toggleModeIcon;

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

        drawerLayout = findViewById(R.id.drawer_layout);
        ImageButton menuButton = findViewById(R.id.menu_button);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(findViewById(R.id.sidebar))) {
                    drawerLayout.closeDrawer(findViewById(R.id.sidebar));
                } else {
                    drawerLayout.openDrawer(findViewById(R.id.sidebar));
                }
            }
        });

        LinearLayout menuHome = findViewById(R.id.menu_home);
        LinearLayout menuShorts = findViewById(R.id.menu_shorts);
        LinearLayout menuSubscriptions = findViewById(R.id.menu_subscriptions);
        LinearLayout menuYou = findViewById(R.id.menu_you);
        LinearLayout menuHistory = findViewById(R.id.menu_history);

        View.OnClickListener menuClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(findViewById(R.id.sidebar));
            }
        };

        menuHome.setOnClickListener(menuClickListener);
        menuShorts.setOnClickListener(menuClickListener);
        menuSubscriptions.setOnClickListener(menuClickListener);
        menuYou.setOnClickListener(menuClickListener);
        menuHistory.setOnClickListener(menuClickListener);

        toggleModeButton = findViewById(R.id.btn_toggle_mode);
        toggleModeIcon = findViewById(R.id.toggle_mode_icon);
        updateModeButtonText(); // Set initial text based on current mode
        toggleModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nightMode = AppCompatDelegate.getDefaultNightMode();
                if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                recreate();
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

    private void updateModeButtonText() {
        int nightMode = AppCompatDelegate.getDefaultNightMode();
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            toggleModeIcon.setImageResource(R.drawable.ic_light_mode);
        } else {
            toggleModeIcon.setImageResource(R.drawable.ic_dark_mode);
        }
    }
}