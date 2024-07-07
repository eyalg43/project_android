package com.example.project_android.entities;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.project_android.dao.VideoDao;
import com.example.project_android.entities.VideoData;

@Database(entities = {VideoData.class}, version = 1)
public abstract class VideoDatabase extends RoomDatabase {

    public abstract VideoDao videoDao();

    private static volatile VideoDatabase INSTANCE;

    public static VideoDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (VideoDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    VideoDatabase.class, "video_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
