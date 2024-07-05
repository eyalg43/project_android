package com.example.project_android.repositories;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.project_android.api.ApiService;
import com.example.project_android.api.RetrofitClient;
import com.example.project_android.dao.VideoDao;
import com.example.project_android.entities.VideoData;
import com.example.project_android.AppDatabase;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoRepository {
    private VideoDao videoDao;
    private LiveData<List<VideoData>> allVideos;
    private ApiService apiService;
    private MutableLiveData<List<VideoData>> serverVideos;

    public VideoRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        videoDao = database.videoDao();
        apiService = RetrofitClient.getApiService();
        allVideos = videoDao.getAllVideos();
        serverVideos = new MutableLiveData<>();
    }

    public LiveData<List<VideoData>> getAllVideos() {
        return allVideos;
    }

    public LiveData<List<VideoData>> getServerVideos() {
        return serverVideos;
    }

    public void insert(VideoData video) {
        new InsertVideoAsyncTask(videoDao).execute(video);
    }

    public void update(VideoData video) {
        new UpdateVideoAsyncTask(videoDao).execute(video);
    }

    public void delete(VideoData video) {
        new DeleteVideoAsyncTask(videoDao).execute(video);
    }

    public void fetchVideosFromServer() {
        apiService.getAllVideos().enqueue(new Callback<List<VideoData>>() {
            @Override
            public void onResponse(Call<List<VideoData>> call, Response<List<VideoData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<VideoData> videos = response.body();
                    Log.d("VideoRepository", "Fetched " + videos.size() + " videos from server.");
                    for (VideoData video : videos) {
                        Log.d("VideoRepository", "Fetched video: " + video.getTitle());
                        insert(video);
                    }
                    serverVideos.postValue(videos);
                } else {
                    Log.e("VideoRepository", "Failed to fetch videos. Response code: " + response.code());
                    try {
                        Log.e("VideoRepository", "Response error body: " + response.errorBody().string());
                    } catch (Exception e) {
                        Log.e("VideoRepository", "Error reading error body: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<VideoData>> call, Throwable t) {
                Log.e("VideoRepository", "Error fetching videos: " + t.getMessage());
            }
        });
    }

    private static class InsertVideoAsyncTask extends AsyncTask<VideoData, Void, Void> {
        private VideoDao videoDao;

        private InsertVideoAsyncTask(VideoDao videoDao) {
            this.videoDao = videoDao;
        }

        @Override
        protected Void doInBackground(VideoData... videos) {
            Log.d("InsertVideoAsyncTask", "Inserting video: " + videos[0].getTitle());
            videoDao.insert(videos[0]);
            return null;
        }
    }

    private static class UpdateVideoAsyncTask extends AsyncTask<VideoData, Void, Void> {
        private VideoDao videoDao;

        private UpdateVideoAsyncTask(VideoDao videoDao) {
            this.videoDao = videoDao;
        }

        @Override
        protected Void doInBackground(VideoData... videos) {
            Log.d("UpdateVideoAsyncTask", "Updating video: " + videos[0].getTitle());
            videoDao.update(videos[0]);
            return null;
        }
    }

    private static class DeleteVideoAsyncTask extends AsyncTask<VideoData, Void, Void> {
        private VideoDao videoDao;

        private DeleteVideoAsyncTask(VideoDao videoDao) {
            this.videoDao = videoDao;
        }

        @Override
        protected Void doInBackground(VideoData... videos) {
            Log.d("DeleteVideoAsyncTask", "Deleting video: " + videos[0].getTitle());
            videoDao.delete(videos[0]);
            return null;
        }
    }
}
