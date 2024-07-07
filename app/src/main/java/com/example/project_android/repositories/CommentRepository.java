package com.example.project_android.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.project_android.AppDatabase;
import com.example.project_android.api.ApiService;
import com.example.project_android.api.RetrofitClient;
import com.example.project_android.dao.CommentDao;
import com.example.project_android.entities.CommentData;

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

    public void createComment(CommentData commentData) {
        apiService.createComment(commentData).enqueue(new Callback<CommentData>() {
            @Override
            public void onResponse(Call<CommentData> call, Response<CommentData> response) {
                if (response.isSuccessful()) {
                    executor.execute(() -> commentDao.insertComment(response.body()));
                }
            }

            @Override
            public void onFailure(Call<CommentData> call, Throwable t) {
                // Handle failure
            }
        });
    }

    public void updateComment(CommentData commentData) {
        apiService.updateComment(commentData.getId(), commentData).enqueue(new Callback<CommentData>() {
            @Override
            public void onResponse(Call<CommentData> call, Response<CommentData> response) {
                if (response.isSuccessful()) {
                    executor.execute(() -> commentDao.updateComment(response.body()));
                }
            }

            @Override
            public void onFailure(Call<CommentData> call, Throwable t) {
                // Handle failure
            }
        });
    }

    public void deleteComment(CommentData commentData) {
        apiService.deleteComment(commentData.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    executor.execute(() -> commentDao.deleteComment(commentData));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
            }
        });
    }
}
