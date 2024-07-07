package com.example.project_android.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.project_android.entities.VideoData;
import com.example.project_android.repositories.VideoRepository;

import java.util.List;

public class VideoViewModel extends AndroidViewModel {
    private VideoRepository repository;
    private LiveData<List<VideoData>> allVideos;

    public VideoViewModel(Application application) {
        super(application);
        repository = new VideoRepository(application);
        allVideos = repository.getAllVideos();
    }

    public LiveData<List<VideoData>> getAllVideos() {
        return allVideos;
    }

    public void fetchVideosFromServer() {
        repository.fetchVideosFromServer();
    }
}
