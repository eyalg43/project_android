package com.example.project_android.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.project_android.entities.VideoData;
import com.example.project_android.repositories.VideoRepository;

import java.util.List;


public class VideoViewModel extends AndroidViewModel {
    private VideoRepository videoRepository;
    private LiveData<List<VideoData>> allVideos;

    public VideoViewModel(Application application) {
        super(application);
        videoRepository = new VideoRepository(application);
        allVideos = videoRepository.getAllVideos();
    }

    public LiveData<List<VideoData>> getAllVideos() {
        return allVideos;
    }


    /*public void add(VideoData videoData) {
        videoRepository.add(videoData);
    }

    public void delete(VideoData videoData) {
        videoRepository.delete(videoData);
    }

    public void reload() {
        videoRepository.reload();
    }*/
}