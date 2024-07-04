package com.example.project_android.api;

import com.example.project_android.entities.CommentData;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.DELETE;
import retrofit2.http.Path;
import retrofit2.Call;



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
}

