package com.example.project_android.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.project_android.TokenManager;
import com.example.project_android.AppDatabase;
import com.example.project_android.UserState;
import com.example.project_android.api.ApiService;
import com.example.project_android.api.RetrofitClient;
import com.example.project_android.dao.CommentDao;
import com.example.project_android.entities.CommentData;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentRepository {
    private CommentDao commentDao;
    private ApiService apiService;
    private Executor executor;
    private String token = TokenManager.getInstance().getToken();

    public CommentRepository(Application application) {
        AppDatabase db = Room.databaseBuilder(application, AppDatabase.class, "database-name").build();
        commentDao = db.commentDao();
        apiService = RetrofitClient.getApiService();
        executor = Executors.newSingleThreadExecutor(); // Single thread executor
    }

    public LiveData<List<CommentData>> getComments(String videoId) {
        MutableLiveData<List<CommentData>> comments = new MutableLiveData<>();
        apiService.getComments().enqueue(new Callback<List<CommentData>>() {
            @Override
            public void onResponse(Call<List<CommentData>> call, Response<List<CommentData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    executor.execute(() -> {
                        for (CommentData comment : response.body()) {
                            comment.setUrlForEmulator();
                        }
                        List<CommentData> filteredComments = filterCommentsByVideoId(response.body(), videoId);
                        comments.postValue(filteredComments);
                    });
                }
            }

            @Override
            public void onFailure(Call<List<CommentData>> call, Throwable t) {
                executor.execute(() -> {
                    List<CommentData> cachedComments = commentDao.getCommentsForVideo(videoId);
                    comments.postValue(cachedComments);
                });
            }
        });
        return comments;
    }

    private List<CommentData> filterCommentsByVideoId(List<CommentData> comments, String videoId) {
        List<CommentData> filteredComments = new ArrayList<>();
        for (CommentData comment : comments) {
            if (comment.getVideoId().equals(videoId)) {
                filteredComments.add(comment);
            }
        }
        return filteredComments;
    }

    // CommentRepository.java
    public void createComment(CommentData commentData) {
        JsonObject commentJson = new JsonObject();
        commentJson.addProperty("text", commentData.getText());
        commentJson.addProperty("userName", commentData.getUsername());
        commentJson.addProperty("userDisplayName", commentData.getDisplayName());
        commentJson.addProperty("date", commentData.getDate());
        commentJson.addProperty("img", commentData.getImg());
        commentJson.addProperty("videoId", commentData.getVideoId());

        Log.d("CommentRepository", "Using token: " + token);

        apiService.createComment("Bearer " + token, commentJson).enqueue(new Callback<CommentData>() {
            @Override
            public void onResponse(Call<CommentData> call, Response<CommentData> response) {
                if (response.isSuccessful()) {
                    executor.execute(() -> commentDao.insertComment(response.body()));
                } else {
                    Log.e("CommentRepository", "Failed to create comment: " + response.message());
                    if (response.code() == 401) {
                        Log.e("CommentRepository", "Unauthorized - logging out");
                        UserState.logOut();
                    }
                }
            }

            @Override
            public void onFailure(Call<CommentData> call, Throwable t) {
                Log.e("CommentRepository", "Error creating comment: " + t.getMessage());
            }
        });
    }

    public void updateComment(CommentData commentData) {
        Log.d("CommentRepository", "Updating comment with ID: " + commentData.getId()); // Add this log for debugging

        Gson gson = new Gson(); // Create a local instance of Gson

        // Manually construct the JSON object
        JsonObject commentJson = new JsonObject();
        commentJson.addProperty("id", commentData.getId());
        commentJson.addProperty("text", commentData.getText());
        commentJson.addProperty("userName", commentData.getUsername());
        commentJson.addProperty("userDisplayName", commentData.getDisplayName());
        commentJson.addProperty("date", commentData.getDate());
        commentJson.addProperty("img", commentData.getImg());
        commentJson.addProperty("videoId", commentData.getVideoId());

        // Add likes and dislikes using Gson
        JsonArray likesArray = gson.toJsonTree(commentData.getLikes()).getAsJsonArray();
        commentJson.add("likes", likesArray);

        JsonArray dislikesArray = gson.toJsonTree(commentData.getDislikes()).getAsJsonArray();
        commentJson.add("dislikes", dislikesArray);

        apiService.updateComment("Bearer " + token, commentData.getId(), commentJson).enqueue(new Callback<CommentData>() {
            @Override
            public void onResponse(Call<CommentData> call, Response<CommentData> response) {
                if (response.isSuccessful()) {
                    executor.execute(() -> {
                        commentDao.updateComment(response.body());
                        refreshComments(commentData.getVideoId());
                    });
                } else {
                    Log.e("CommentRepository", "Failed to update comment: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<CommentData> call, Throwable t) {
                Log.e("CommentRepository", "Error updating comment: " + t.getMessage());
            }
        });
    }

    public void deleteComment(CommentData commentData) {
        apiService.deleteComment("Bearer " + token, commentData.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    executor.execute(() -> {
                        commentDao.deleteComment(commentData);
                        refreshComments(commentData.getVideoId());
                    });
                } else {
                    Log.e("CommentRepository", "Failed to delete comment: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("CommentRepository", "Error deleting comment: " + t.getMessage());
            }
        });
    }

    private void refreshComments(String videoId) {
        apiService.getComments().enqueue(new Callback<List<CommentData>>() {
            @Override
            public void onResponse(Call<List<CommentData>> call, Response<List<CommentData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    executor.execute(() -> {
                        for (CommentData comment : response.body()) {
                            comment.setUrlForEmulator();
                        }
                        List<CommentData> filteredComments = filterCommentsByVideoId(response.body(), videoId);
                        commentDao.insertComments(filteredComments);
                    });
                }
            }

            @Override
            public void onFailure(Call<List<CommentData>> call, Throwable t) {
                Log.e("CommentRepository", "Error refreshing comments: " + t.getMessage());
            }
        });
    }
}
