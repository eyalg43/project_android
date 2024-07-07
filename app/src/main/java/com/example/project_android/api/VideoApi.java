package com.example.project_android.api;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.project_android.entities.VideoData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class VideoApi {
    private ApiService apiService;

    public VideoApi() {
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        apiService = retrofit.create(ApiService.class);
    }

    public void getAllVideos(MutableLiveData<List<VideoData>> videos) {
        Call<List<VideoData>> call = apiService.getAllVideos();
        call.enqueue(new Callback<List<VideoData>>() {
            @Override
            public void onResponse(Call<List<VideoData>> call, Response<List<VideoData>> response) {
                videos.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<VideoData>> call, Throwable t) {

            }
        });
    }

    public void getVideoById(String videoId, MutableLiveData<VideoData> video) {
        Call<VideoData> call = apiService.getVideo(videoId);
        call.enqueue(new Callback<VideoData>() {
            @Override
            public void onResponse(Call<VideoData> call, Response<VideoData> response) {
                video.postValue(response.body());
            }

            @Override
            public void onFailure(Call<VideoData> call, Throwable t) {
                // Handle failure
            }
        });
    }

}