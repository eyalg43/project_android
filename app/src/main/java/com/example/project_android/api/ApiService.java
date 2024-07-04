package com.example.project_android.api;

import com.example.project_android.entities.VideoData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("api/videos")
    Call<List<VideoData>> getVideos();
}