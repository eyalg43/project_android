package com.example.project_android.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.project_android.entities.CommentData;

import java.util.List;

@Dao
public interface CommentDao {

    @Query("SELECT * FROM comments WHERE videoId = :videoId")
    List<CommentData> getCommentsForVideo(String videoId);

    @Query("SELECT * FROM comments WHERE _id = :commentId")
    CommentData getCommentById(String commentId);

    @Insert
    void insertComment(CommentData comment);

    @Update
    void updateComment(CommentData comment);

    @Delete
    void deleteComment(CommentData comment);
}
