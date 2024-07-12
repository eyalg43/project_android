package com.example.project_android.repositories;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.project_android.AppDatabase;
import com.example.project_android.api.VideoApi;
import com.example.project_android.dao.VideoDao;
import com.example.project_android.entities.VideoData;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class VideoRepository {
    private VideoApi videoApi;
    private MutableLiveData<List<VideoData>> allVideos;

    public VideoRepository() {
        videoApi = new VideoApi();
        allVideos = new MutableLiveData<>();
    }

    public LiveData<List<VideoData>> getAllVideos() {
        syncWithServer();
        return allVideos;
    }

    public LiveData<VideoData> getVideoById(String videoId) {
        MutableLiveData<VideoData> videoData = new MutableLiveData<>();
        videoApi.getVideoById(videoId, videoData);
        return videoData;
    }

    public void syncWithServer() {
        MutableLiveData<List<VideoData>> remoteVideos = new MutableLiveData<>();
        videoApi.getAllVideos(remoteVideos);

        remoteVideos.observeForever(new Observer<List<VideoData>>() {
            @Override
            public void onChanged(List<VideoData> videos) {
                if (videos != null) {
                    for (VideoData video : videos) {
                        video.setUrlForEmulator();
                    }
                    allVideos.setValue(videos);
                    remoteVideos.removeObserver(this);  // Remove observer after getting data
                }
            }
        });
    }

    public LiveData<VideoData> uploadVideo(String token, String userId, File imgFile, File videoFile, String title, String description, String author, String username, String authorImage, String uploadTime) {
        MutableLiveData<VideoData> uploadedVideo = new MutableLiveData<>();
        videoApi.uploadVideo(token, userId, imgFile, videoFile, title, description, author, username, authorImage, uploadTime, new VideoApi.UploadCallback() {
            @Override
            public void onSuccess(VideoData videoData) {
                uploadedVideo.postValue(videoData);
            }

            @Override
            public void onFailure(String error) {
                Log.e("VideoRepository", "Upload failed: " + error);
            }
        });
        return uploadedVideo;
    }


    public LiveData<VideoData> updateVideo(String token, String userId, String videoId, File imgFile, File videoFile, String title, String description) {
        MutableLiveData<VideoData> updatedVideo = new MutableLiveData<>();
        videoApi.updateVideo(token, userId, videoId, imgFile, videoFile, title, description, new VideoApi.UploadCallback() {
            @Override
            public void onSuccess(VideoData videoData) {
                updatedVideo.postValue(videoData);
            }

            @Override
            public void onFailure(String error) {
                Log.e("VideoRepository", "Update failed: " + error);
            }
        });
        return updatedVideo;
    }

    public LiveData<Boolean> deleteVideo(String token, String userId, String videoId) {
        MutableLiveData<Boolean> deleteResult = new MutableLiveData<>();
        videoApi.deleteVideo(token, userId, videoId, new VideoApi.DeleteCallback() {
            @Override
            public void onSuccess() {
                deleteResult.postValue(true);
            }

            @Override
            public void onFailure(String error) {
                Log.e("VideoRepository", "Delete failed: " + error);
                deleteResult.postValue(false);
            }
        });
        return deleteResult;
    }


    /*public void add(final VideoData video) {
        videoApi.add(video);
    }

    public void delete(final VideoData video) {
        videoApi.delete(video);
    }

    public void reload() {
        videoApi.getAllVideos();
    }*/
}
