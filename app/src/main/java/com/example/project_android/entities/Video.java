package com.example.project_android.entities;

public class Video {
    private int id;
    private String title;
    private String description;
    private String author;
    private int views;
    private int img;
    private String video;
    private String uploadTime;
    private int authorImage;

    public Video(int id, String title, String description, String author,
                 int views, int img, String video, String uploadTime, int authorImage) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.views = views;
        this.img = img;
        this.video = video;
        this.uploadTime = uploadTime;
        this.authorImage = authorImage;
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

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
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

    public int getAuthorImage() {
        return authorImage;
    }

    public void setAuthorImage(int authorImage) {
        this.authorImage = authorImage;
    }
}
