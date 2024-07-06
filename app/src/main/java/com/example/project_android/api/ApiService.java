package com.example.project_android.api;

import com.example.project_android.entities.CommentData;
import com.example.project_android.entities.User;
import com.example.project_android.entities.VideoData;
import com.google.gson.JsonElement;

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
    @PATCH("videos/{id}/like")
    Call<Void> likeVideo(@Path("id") int videoId);

    @PATCH("videos/{id}/dislike")
    Call<Void> dislikeVideo(@Path("id") int videoId);

    @GET("comments")
    Call<List<CommentData>> getComments();

    @POST("comments")
    Call<CommentData> createComment(@Body CommentData comment);

    @GET("comments/{id}")
    Call<CommentData> getComment(@Path("id") int commentId);

    @PATCH("users/{id}/comments")
    Call<CommentData> updateComment(@Path("id") int userId, @Body CommentData comment);

    @DELETE("users/{id}/comments")
    Call<Void> deleteComment(@Path("id") int userId);

    @PATCH("{id}/like")
    Call<Void> likeComment(@Path("id") int commentId);

    @PATCH("{id}/dislike")
    Call<Void> dislikeComment(@Path("id") int commentId);

    // Video-related endpoints
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

    @POST("api/tokens")
    Call<JsonElement> generateToken(@Body User user);
}