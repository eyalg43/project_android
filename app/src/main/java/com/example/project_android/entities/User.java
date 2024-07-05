package com.example.project_android.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey
    @NonNull
    private String username;
    private String password;
    private String displayName;
    private String imageUri;  // Added imageUri field

    public User(String username, String password, String displayName, String imageUri) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.imageUri = imageUri;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getImageUri() {
        return imageUri;
    }
}
