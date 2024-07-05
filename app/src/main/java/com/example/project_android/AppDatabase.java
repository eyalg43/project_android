package com.example.project_android;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.project_android.dao.CommentDao;
import com.example.project_android.entities.CommentData;

@Database(entities = {CommentData.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract CommentDao commentDao();
}
