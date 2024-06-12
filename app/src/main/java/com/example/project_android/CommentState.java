package com.example.project_android;

import com.example.project_android.entities.CommentData;

import java.util.HashMap;
import java.util.Map;

public class CommentState {

    private static CommentState instance;
    private Map<Integer, CommentData> commentDataMap;

    private CommentState() {
        commentDataMap = new HashMap<>();
    }

    public static CommentState getInstance() {
        if (instance == null) {
            instance = new CommentState();
        }
        return instance;
    }

    public CommentData getCommentData(int id) {
        return commentDataMap.get(id);
    }

    public void addCommentData(CommentData commentData) {
        commentDataMap.put(commentData.getId(), commentData);
    }

    public void updateCommentData(CommentData updatedCommentData) {
        commentDataMap.put(updatedCommentData.getId(), updatedCommentData);
    }
}
