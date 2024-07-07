package com.example.project_android.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.project_android.entities.VideoData;
import com.example.project_android.repositories.VideoRepository;

import java.util.List;


public class VideoViewModel extends ViewModel {
    private VideoRepository videoRepository;
    private LiveData<List<VideoData>> allVideos;

    public VideoViewModel() {
        videoRepository = new VideoRepository();
        allVideos = videoRepository.getAllVideos();
    }

    public LiveData<List<VideoData>> getAllVideos() {
        return allVideos;
    }

    public LiveData<VideoData> getVideoById(String videoId) {
        return videoRepository.getVideoById(videoId);
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

