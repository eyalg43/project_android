package com.example.project_android.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.project_android.entities.CommentData;
import com.example.project_android.repositories.CommentRepository;

import java.util.List;

public class CommentViewModel extends AndroidViewModel {
    private CommentRepository commentRepository;
    private LiveData<List<CommentData>> comments;

    public CommentViewModel(@NonNull Application application) {
        super(application);
        commentRepository = new CommentRepository(application);
    }

    public LiveData<List<CommentData>> getComments(int videoId) {
        comments = commentRepository.getComments(videoId);
        return comments;
    }

    public void createComment(CommentData commentData) {
        commentRepository.createComment(commentData);
    }

    public void updateComment(CommentData commentData) {
        commentRepository.updateComment(commentData);
    }

    public void deleteComment(CommentData commentData) {
        commentRepository.deleteComment(commentData);
    }
}
