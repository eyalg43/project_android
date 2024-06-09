package com.example.project_android;

import java.util.List;

public class VideoComments {
    private String videoId;
    private List<CommentData> comments;

    // Getters and Setters
    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public List<CommentData> getComments() {
        return comments;
    }

    public void setComments(List<CommentData> comments) {
        this.comments = comments;
    }
}
// Path: app/src/main/res/layout/activity_video_comments.xml