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
