package com.example.project_android.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.project_android.entities.VideoData;

import java.util.List;

@Dao
public interface VideoDao {
    @Query("SELECT * FROM videos")
    LiveData<List<VideoData>> getAllVideos();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVideos(List<VideoData> videos);

    @Query("DELETE FROM videos")
    void deleteAllVideos();
}
