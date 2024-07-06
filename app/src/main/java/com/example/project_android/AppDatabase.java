package com.example.project_android;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.project_android.dao.CommentDao;
import com.example.project_android.dao.UserDao;
import com.example.project_android.dao.VideoDao;
import com.example.project_android.entities.CommentData;
import com.example.project_android.entities.User;
import com.example.project_android.entities.VideoData;

@Database(entities = {VideoData.class, User.class, CommentData.class}, version = 2) // Incremented version number
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract VideoDao videoDao();
    public abstract UserDao userDao();
    public abstract CommentDao commentDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                    .addMigrations(MIGRATION_1_2) // Add your migration here
                    .build();
        }
        return instance;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Migration strategy
            database.execSQL("ALTER TABLE VideoData ADD COLUMN imgPath TEXT");
            database.execSQL("ALTER TABLE VideoData ADD COLUMN videoPath TEXT");
            database.execSQL("ALTER TABLE VideoData ADD COLUMN authorImagePath TEXT");
        }
    };
}
