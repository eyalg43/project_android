package com.example.project_android.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.project_android.api.VideoApi;
import com.example.project_android.entities.VideoData;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class VideoRepository {
    private VideoListData videoListData;
    private VideoApi videoApi;

    public VideoRepository() {
        videoListData = new VideoListData();
        videoApi = new VideoApi();
    }

    class VideoListData extends MutableLiveData<List<VideoData>> {
        public VideoListData() {
            super();
            List<VideoData> videos = new LinkedList<VideoData>();
            videos.add(new VideoData("Video 1", "yes", "Menahem", "Sir", "1008", "", "", "Yesterday", "", new ArrayList<>(), new ArrayList<>()));
            setValue(videos);
        }

        @Override
        protected void onActive() {
            super.onActive();

            /* new Thread( {
                videoListData.postValue(videoApi.getAllVideos());
            }).start(); */

            VideoApi videoApi = new VideoApi();
            videoApi.getAllVideos(this);
        }
    }

    public LiveData<List<VideoData>> getAllVideos() {
        return videoListData;
    }

    public LiveData<VideoData> getVideoById(String videoId) {
        MutableLiveData<VideoData> videoData = new MutableLiveData<>();
        videoApi.getVideoById(videoId, videoData);
        return videoData;
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
