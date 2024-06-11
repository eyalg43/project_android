package com.example.project_android;

import android.util.Log;

import com.example.project_android.entities.VideoData;
import java.util.ArrayList;
import java.util.List;

public class VideosState {

    private static VideosState instance;
    private List<VideoData> videoList;

    private VideosState() {
        videoList = new ArrayList<>();
    }

    public static VideosState getInstance() {
        if (instance == null) {
            instance = new VideosState();
        }
        return instance;
    }

    public List<VideoData> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<VideoData> videoList) {
        this.videoList = videoList;
    }

    public void addVideo(VideoData videoData) {
        videoList.add(videoData);
        Log.d("VideosState", "Video added successfully: " + videoData.getTitle());
    }
}
