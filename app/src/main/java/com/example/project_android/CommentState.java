package com.example.project_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.project_android.entities.CommentData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentState {

    private static CommentState instance;
    private Map<Integer, List<CommentData>> commentDataMap;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    private CommentState(Context context) {
        commentDataMap = new HashMap<>();
        sharedPreferences = context.getSharedPreferences("comments_prefs", Context.MODE_PRIVATE);
        gson = new Gson();
        loadCommentsFromPrefs();
    }

    public static CommentState getInstance(Context context) {
        if (instance == null) {
            instance = new CommentState(context);
        }
        return instance;
    }

    public List<CommentData> getCommentsForVideo(int videoId) {
        return commentDataMap.get(videoId);
    }

    public void updateCommentsForVideo(int videoId, List<CommentData> comments) {
        commentDataMap.put(videoId, comments);
        saveCommentsToPrefs();
    }

    public CommentData getCommentData(int id) {
        for (List<CommentData> comments : commentDataMap.values()) {
            for (CommentData comment : comments) {
                if (comment.getId() == id) {
                    return comment;
                }
            }
        }
        return null;
    }

    public void addCommentsForVideo(int videoId, List<CommentData> comments) {
        commentDataMap.put(videoId, comments);
        saveCommentsToPrefs();
    }

    public void updateCommentData(CommentData updatedCommentData) {
        int videoId = getVideoIdForComment(updatedCommentData.getId());
        if (videoId != -1) {
            List<CommentData> comments = commentDataMap.get(videoId);
            for (int i = 0; i < comments.size(); i++) {
                if (comments.get(i).getId() == updatedCommentData.getId()) {
                    comments.set(i, updatedCommentData);
                    break;
                }
            }
            updateCommentsForVideo(videoId, comments); // Ensure state is updated
        }
    }

    public void deleteCommentData(int commentId) {
        int videoId = getVideoIdForComment(commentId);
        if (videoId != -1) {
            List<CommentData> comments = commentDataMap.get(videoId);
            comments.removeIf(comment -> comment.getId() == commentId);
            updateCommentsForVideo(videoId, comments); // Ensure state is updated
        }
    }

    private int getVideoIdForComment(int commentId) {
        for (Map.Entry<Integer, List<CommentData>> entry : commentDataMap.entrySet()) {
            for (CommentData comment : entry.getValue()) {
                if (comment.getId() == commentId) {
                    return entry.getKey();
                }
            }
        }
        return -1;
    }

    public Map<Integer, List<CommentData>> getAllComments() {
        return commentDataMap;
    }

    private void saveCommentsToPrefs() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(commentDataMap);
        editor.putString("comments_data", json);
        editor.apply();
    }

    private void loadCommentsFromPrefs() {
        String json = sharedPreferences.getString("comments_data", null);
        if (json != null) {
            Type type = new TypeToken<Map<Integer, List<CommentData>>>() {}.getType();
            commentDataMap = gson.fromJson(json, type);
        }
    }
}
