package com.example.project_android.repositories;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.project_android.api.ApiService;
import com.example.project_android.api.RetrofitClient;
import com.example.project_android.dao.VideoDao;
import com.example.project_android.entities.VideoData;
import com.example.project_android.AppDatabase;
import com.example.project_android.entities.VideoDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoRepository {
    private VideoDao videoDao;
    private ApiService apiService;
    private LiveData<List<VideoData>> allVideos;

    public VideoRepository(Application application) {
        VideoDatabase database = VideoDatabase.getDatabase(application);
        videoDao = database.videoDao();
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        allVideos = videoDao.getAllVideos();
    }

    public LiveData<List<VideoData>> getAllVideos() {
        return allVideos;
    }

    public void fetchVideosFromServer() {
        apiService.getAllVideos().enqueue(new Callback<List<VideoData>>() {
            @Override
            public void onResponse(Call<List<VideoData>> call, Response<List<VideoData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    new Thread(() -> videoDao.insertAll(response.body())).start();
                }
            }

            @Override
            public void onFailure(Call<List<VideoData>> call, Throwable t) {
                // Handle failure
            }
        });
    }
}

