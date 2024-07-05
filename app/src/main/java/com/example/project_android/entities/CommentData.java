package com.example.project_android.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CommentData {
    @PrimaryKey
    private int id;
    private String text;
    private String username;
    private String date;
    private String img;
    private boolean isLiked;
    private boolean isDisliked;

    // Default constructor
    public CommentData() {
    }

    // Constructor with parameters
    public CommentData(int id, String text, String username, String date, String img) {
        this.id = id;
        this.text = text;
        this.username = username;
        this.date = date;
        this.img = img;
        this.isLiked = false;
        this.isDisliked = false;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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
