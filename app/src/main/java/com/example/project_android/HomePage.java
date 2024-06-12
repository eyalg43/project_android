package com.example.project_android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.project_android.adapters.VideosListAdapter;
import com.example.project_android.entities.VideoData;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
                allVideos.remove(video);
                adapter.setVideos(allVideos);
                VideosState.getInstance().setVideoList(allVideos);
            }


        });
        listVideos.setAdapter(adapter);
        listVideos.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout = findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        allVideos = VideosState.getInstance().getVideoList();
        if (allVideos != null) {
            adapter.setVideos(allVideos);
        } else {
            Log.e("HomePage", "Error getting videos");
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

        signInButton = findViewById(R.id.btn_sign_in);
        signUpButton = findViewById(R.id.btn_sign_up);
        authButtonsContainer = findViewById(R.id.auth_buttons_container);
        profileImage = findViewById(R.id.profile_image);
        profileContainer = findViewById(R.id.profile_container);
        welcomeMessage = findViewById(R.id.welcome_message);
        signOutButton = findViewById(R.id.btn_sign_out);
        uploadVideoButton = findViewById(R.id.btn_upload_video);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserState.logout();
                authButtonsContainer.setVisibility(View.VISIBLE);
                profileContainer.setVisibility(View.GONE);
                uploadVideoButton.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
        });

        uploadVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, UploadVideo.class);
                startActivity(intent);
            }
        });

        // Check if user is logged in
        if (UserState.isLoggedIn()) {
            User loggedInUser = UserState.getLoggedInUser();
            if (loggedInUser != null) {
                authButtonsContainer.setVisibility(View.GONE);
                profileContainer.setVisibility(View.VISIBLE);
                uploadVideoButton.setVisibility(View.VISIBLE);
                welcomeMessage.setText("Welcome " + loggedInUser.getDisplayName() + "!");

                // Load the profile image from the app's internal storage
                loadImageFromLocalPath(loggedInUser.getImageUri(), profileImage);
            }
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        updateVideos();
    }

    private void updateVideos() {
        allVideos = VideosState.getInstance().getVideoList();
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
