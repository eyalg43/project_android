package com.example.project_android.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.project_android.api.VideoApi;
import com.example.project_android.entities.VideoData;
import com.example.project_android.repositories.VideoRepository;

import java.io.File;
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

    public void uploadVideo(String token, String userId, File imgFile, File videoFile, String title, String description, String author, String username, String authorImage, String uploadTime, VideoApi.UploadCallback callback) {
        videoRepository.uploadVideo(token, userId, imgFile, videoFile, title, description, author, username, authorImage, uploadTime, callback);
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