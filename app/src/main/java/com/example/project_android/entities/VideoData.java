package com.example.project_android.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class VideoData {
    @PrimaryKey
    private int id;
    private String title;
    private String description;
    private String author;
    private String views;
    private String img;
    private String video;
    private String uploadTime;
    private String authorImage;
    private boolean isLiked;
    private boolean isDisliked;

    public VideoData(int id, String title, String description, String author,
                     String views, String img, String video, String uploadTime, String authorImage) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.views = views;
        this.img = img;
        this.video = video;
        this.uploadTime = uploadTime;
        this.authorImage = authorImage;
        this.isLiked = false;
        this.isDisliked = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getAuthorImage() {
        return authorImage;
    }

    public void setAuthorImage(String authorImage) {
        this.authorImage = authorImage;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isDisliked() {
        return isDisliked;
    }

    public void setDisliked(boolean disliked) {
        isDisliked = disliked;
    }
}
