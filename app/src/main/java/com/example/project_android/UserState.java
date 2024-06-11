package com.example.project_android;

import java.util.ArrayList;
import java.util.List;

public class UserState {
    private static List<User> users = new ArrayList<>();

    private static User userInstance;

    public static void addUser(String username, String password, String displayName, String imageUri) {
        users.add(new User(username, password, displayName, imageUri));
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
                userInstance = user;
                return true;
            }
        }
        return false;
    }
}
