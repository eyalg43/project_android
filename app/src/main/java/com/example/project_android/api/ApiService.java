package com.example.project_android.api;

import com.example.project_android.entities.VideoData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @GET("api/videos")
    Call<List<VideoData>> getVideos();

    @GET("api/allvideos")
    Call<List<VideoData>> getAllVideos();

    @GET("api/videos/{id}")
    Call<VideoData> getVideo(@Path("id") int id);

    @GET("api/users/{id}/videos")
    Call<List<VideoData>> getVideosByAuthor(@Path("id") String userId);

    @POST("api/users/{id}/videos")
    Call<VideoData> createVideo(@Path("id") String userId, @Body VideoData videoData);

    @PATCH("api/users/{id}/videos/{pid}")
    Call<VideoData> updateVideo(@Path("id") String userId, @Path("pid") int videoId, @Body VideoData videoData);

    @DELETE("api/users/{id}/videos/{pid}")
    Call<Void> deleteVideo(@Path("id") String userId, @Path("pid") int videoId);

    @PATCH("api/videos/{id}/like")
    Call<VideoData> likeVideo(@Path("id") int videoId, @Body String userDisplayName);

    @PATCH("api/videos/{id}/dislike")
    Call<VideoData> dislikeVideo(@Path("id") int videoId, @Body String userDisplayName);
}