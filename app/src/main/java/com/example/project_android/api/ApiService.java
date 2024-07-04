package com.example.project_android.api;

import com.example.project_android.entities.User;
import com.example.project_android.entities.VideoData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    // Video-related endpoints
    @GET("api/videos")
    Call<List<VideoData>> getVideos();

    // User-related endpoints
    @POST("api/users")
    Call<User> createUser(@Body User user);

    @POST("api/users/signin")
    Call<User> signInUser(@Body User user);

    @GET("api/users")
    Call<List<User>> getUsers(@Header("Authorization") String token);

    @GET("api/users/{id}")
    Call<User> getUser(@Header("Authorization") String token, @Path("id") String userId);

    @DELETE("api/users/{id}")
    Call<Void> deleteUser(@Header("Authorization") String token, @Path("id") String userId);

    @PATCH("api/users/{id}")
    Call<User> updateUser(@Header("Authorization") String token, @Path("id") String userId, @Body User user);
}
