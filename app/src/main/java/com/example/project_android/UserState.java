package com.example.project_android;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class UserState {
    private static List<User> users = new ArrayList<>();
    private static User loggedInUser;

    private UserState() { }

    public static void addUser(String username, String password, String displayName, String imageUri) {
        users.add(new User(username, password, displayName, imageUri));
    }

    public static void setUser(User user) {
        loggedInUser = user;
        Log.e("UserState", "User set successfully: " + user.getUsername() + " - " + user.getDisplayName());
    }

    public static User getUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static boolean validateLogin(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                loggedInUser = user;
                return true;
            }
        }
        return false;
    }

    public static boolean isLoggedIn() {
        return loggedInUser != null;
    }

    public static void logout() {
        loggedInUser = null;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }
}
