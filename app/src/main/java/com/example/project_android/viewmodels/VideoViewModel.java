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
    private LiveData<List<VideoData>> serverVideos;

    public VideoViewModel(@NonNull Application application) {
        super(application);
        repository = new VideoRepository(application);
        allVideos = repository.getAllVideos();
        serverVideos = repository.getServerVideos();
    }

    public LiveData<List<VideoData>> getAllVideos() {
        return allVideos;
    }

    public LiveData<List<VideoData>> getServerVideos() {
        return serverVideos;
    }

    public void insert(VideoData video) {
        repository.insert(video);
    }

    public void update(VideoData video) {
        repository.update(video);
    }

    public void delete(VideoData video) {
        repository.delete(video);
    }

    public void fetchVideosFromServer() {
        repository.fetchVideosFromServer();
    }
}
