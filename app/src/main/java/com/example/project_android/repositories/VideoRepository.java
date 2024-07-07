package com.example.project_android.repositories;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.project_android.AppDatabase;
import com.example.project_android.api.VideoApi;
import com.example.project_android.dao.VideoDao;
import com.example.project_android.entities.VideoData;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class VideoRepository {
    private VideoDao videoDao;
    private VideoApi videoApi;
    private LiveData<List<VideoData>> allVideos;

    public VideoRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        videoDao = database.videoDao();
        videoApi = new VideoApi();
        allVideos = videoDao.getAllVideos();
        syncWithServer();
    }

    public LiveData<List<VideoData>> getAllVideos() {
        return allVideos;
    }

    public void syncWithServer() {
        MutableLiveData<List<VideoData>> remoteVideos = new MutableLiveData<>();
        videoApi.getAllVideos(remoteVideos);

        remoteVideos.observeForever(videos -> {
            if (videos != null) {
                insertVideos(videos);
            }
        });
    }

    public void insertVideos(List<VideoData> videos) {
        new InsertVideosAsyncTask(videoDao).execute(videos);
    }

    private static class InsertVideosAsyncTask extends AsyncTask<List<VideoData>, Void, Void> {
        private VideoDao videoDao;

        private InsertVideosAsyncTask(VideoDao videoDao) {
            this.videoDao = videoDao;
        }

        @Override
        protected Void doInBackground(List<VideoData>... lists) {
            videoDao.deleteAllVideos();
            videoDao.insertVideos(lists[0]);
            return null;
        }
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
