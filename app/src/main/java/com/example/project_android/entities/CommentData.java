package com.example.project_android.entities;

public class CommentData {
    private int id;
    private String text;
    private String username;
    private String date;
    private String img;

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
}
