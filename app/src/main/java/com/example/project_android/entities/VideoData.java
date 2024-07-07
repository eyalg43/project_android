package com.example.project_android.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.project_android.Converters;

import java.util.List;

@Entity(tableName = "VideoData")
public class VideoData {
    @PrimaryKey
    @NonNull
    private String _id;
    private String title;
    private String description;
    private String author;
    private String views;
    private String imgPath; // Path to image file
    private String videoPath; // Path to video file
    private String uploadTime;
    private String authorImagePath; // Path to author image file

    @TypeConverters(Converters.class)
    private List<String> likes;

    @TypeConverters(Converters.class)
    private List<String> dislikes;

    private String img; // Base64 encoded image data
    private String video; // Base64 encoded video data
    private String authorImage; // Base64 encoded author image data

    public VideoData(String title, String description, String author,
                     String views, String imgPath, String videoPath, String uploadTime, String authorImagePath,
                     String img, String video, String authorImage, List<String> likes, List<String> dislikes) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.views = views;
        this.imgPath = imgPath;
        this.videoPath = videoPath;
        this.uploadTime = uploadTime;
        this.authorImagePath = authorImagePath;
        this.img = img;
        this.video = video;
        this.authorImage = authorImage;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public String getId() {
        return _id;
    }

    public void setId(@NonNull String _id) {
        this._id = _id;
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

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getAuthorImagePath() {
        return authorImagePath;
    }

    public void setAuthorImagePath(String authorImagePath) {
        this.authorImagePath = authorImagePath;
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

    public String getAuthorImage() {
        return authorImage;
    }

    public void setAuthorImage(String authorImage) {
        this.authorImage = authorImage;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public List<String> getDislikes() {
        return dislikes;
    }

    public void setDislikes(List<String> dislikes) {
        this.dislikes = dislikes;
    }
}


