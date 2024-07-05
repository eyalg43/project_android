package com.example.project_android.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.project_android.entities.User;
import com.example.project_android.repositories.UsersRepository;

import java.util.List;

public class UserViewmodel extends AndroidViewModel {
    private UsersRepository usersRepository;
    private LiveData<List<User>> users;

    public UserViewmodel(@NonNull Application application) {
        super(application);
        usersRepository = new UsersRepository(application);
        users = usersRepository.getAllUsers();
    }

    public LiveData<List<User>> getUsers() {
        return users;
    }

    public void insertUser(User user) {
        usersRepository.insertUser(user);
    }

    public void deleteUser(User user) {
        usersRepository.deleteUser(user);
    }

    public void updateUser(User user) {
        usersRepository.updateUser(user);
    }
}
