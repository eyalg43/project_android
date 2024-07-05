package com.example.project_android.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.project_android.AppDatabase;
import com.example.project_android.api.ApiService;
import com.example.project_android.api.RetrofitClient;
import com.example.project_android.dao.CommentDao;
import com.example.project_android.entities.CommentData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentRepository {
    private CommentDao commentDao;
    private ApiService apiService;

    public CommentRepository(Application application) {
        AppDatabase db = Room.databaseBuilder(application, AppDatabase.class, "database-name").build();
        commentDao = db.commentDao();
        apiService = RetrofitClient.getApiService();
    }

    public LiveData<List<CommentData>> getComments(int videoId) {
        MutableLiveData<List<CommentData>> comments = new MutableLiveData<>();
        apiService.getComments(videoId).enqueue(new Callback<List<CommentData>>() {
            @Override
            public void onResponse(Call<List<CommentData>> call, Response<List<CommentData>> response) {
                if (response.isSuccessful()) {
                    for (CommentData comment : response.body()) {
                        commentDao.insertComment(comment);
                    }
                    comments.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<CommentData>> call, Throwable t) {
                comments.postValue(commentDao.getCommentsForVideo(videoId));
            }
        });
        return comments;
    }

    public void createComment(CommentData commentData) {
        apiService.createComment(commentData).enqueue(new Callback<CommentData>() {
            @Override
            public void onResponse(Call<CommentData> call, Response<CommentData> response) {
                if (response.isSuccessful()) {
                    commentDao.insertComment(response.body());
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
                    commentDao.updateComment(response.body());
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
                    commentDao.deleteComment(commentData);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
            }
        });
    }
}
