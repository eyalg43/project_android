package com.example.project_android.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.project_android.entities.VideoData;
import com.example.project_android.repositories.VideoRepository;

import java.io.File;
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

    public LiveData<VideoData> getVideoById(String videoId) {
        return videoRepository.getVideoById(videoId);
    }

    public LiveData<List<VideoData>> getLimitedVideos() {
        return videoRepository.getLimitedVideos();
    }

    public LiveData<VideoData> uploadVideo(String token, String userId, File imgFile, File videoFile, String title, String description, String author, String username, String authorImage, String uploadTime) {
        return videoRepository.uploadVideo(token, userId, imgFile, videoFile, title, description, author, username, authorImage, uploadTime);
    }

    public LiveData<VideoData> updateVideo(String token, String userId, String videoId, File imgFile, File videoFile, String title, String description) {
        return videoRepository.updateVideo(token, userId, videoId, imgFile, videoFile, title, description);
    }

    public LiveData<Boolean> deleteVideo(String token, String userId, String videoId) {
        return videoRepository.deleteVideo(token, userId, videoId);
    }

    public void syncWithServerAfterDeletion() {
        videoRepository.syncWithServer();
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

    public void likeVideo(String videoId, String userDisplayName) {
        videoRepository.likeVideo(videoId, userDisplayName);
    }

    public void dislikeVideo(String videoId, String userDisplayName) {
        videoRepository.dislikeVideo(videoId, userDisplayName);
    }
}