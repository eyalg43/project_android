package com.example.project_android.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.project_android.entities.VideoData;

import java.util.List;

@Dao
public interface VideoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<VideoData> videos);

    @Query("SELECT * FROM VideoData")
    LiveData<List<VideoData>> getAllVideos();

    @Query("SELECT * FROM VideoData WHERE _id = :id")
    VideoData get(String id);


    @Update
    void update(VideoData... videoData);

    @Delete
    void delete(VideoData... videoData);
}
