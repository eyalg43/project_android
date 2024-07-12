package com.example.project_android.api;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.project_android.entities.VideoData;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    public void uploadVideo(String token, String userId, File imgFile, File videoFile, String title, String description, String author, String username, String authorImage, String uploadTime, UploadCallback callback) {
        RequestBody titlePart = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody descriptionPart = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody authorPart = RequestBody.create(MediaType.parse("text/plain"), author);
        RequestBody usernamePart = RequestBody.create(MediaType.parse("text/plain"), username);
        RequestBody authorImagePart = RequestBody.create(MediaType.parse("text/plain"), authorImage);
        RequestBody uploadTimePart = RequestBody.create(MediaType.parse("text/plain"), uploadTime);

        RequestBody imgBody = RequestBody.create(MediaType.parse("image/*"), imgFile);
        MultipartBody.Part imgPart = MultipartBody.Part.createFormData("img", imgFile.getName(), imgBody);

        RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
        MultipartBody.Part videoPart = MultipartBody.Part.createFormData("video", videoFile.getName(), videoBody);

        Call<VideoData> call = apiService.createVideo(token, userId, imgPart, videoPart, titlePart, descriptionPart, authorPart, usernamePart, authorImagePart, uploadTimePart);
        call.enqueue(new Callback<VideoData>() {
            @Override
            public void onResponse(Call<VideoData> call, Response<VideoData> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Upload failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<VideoData> call, Throwable t) {
                callback.onFailure("Upload failed: " + t.getMessage());
            }
        });
    }

    public interface UploadCallback {
        void onSuccess(VideoData videoData);
        void onFailure(String error);
    }

}