package com.example.project_android.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.project_android.api.ApiService;
import com.example.project_android.api.RetrofitClient;

import com.example.project_android.AppDatabase;
import com.example.project_android.dao.UserDao;
import com.example.project_android.entities.User;

import java.util.LinkedList;
import java.util.List;
import retrofit2.Call;


public class UsersRepository {
    private UserDao userDao;
    private LiveData<List<User>> users;
    private ApiService apiService;

    public UsersRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        userDao = database.userDao();
        apiService = RetrofitClient.getApiService();
        users = userDao.getAllUsers();
    }

    public LiveData<List<User>> getUsers() {
        return users;
    }

    public void insertUser(User user) {
        new InsertUserAsyncTask(userDao).execute(user);
    }

    public void updateUser(User user) {
        new UpdateUserAsyncTask(userDao).execute(user);
    }

    public void deleteUser(User user) {
        new DeleteUserAsyncTask(userDao).execute(user);
    }

    public LiveData<User> getUserById(String userId) {
        return userDao.getUserById(userId);
    }

    public LiveData<User> getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    public LiveData<List<User>> getAllUsers() {
        return userDao.getAllUsers();
    }

    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;
        private InsertUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.insertUser(users[0]);
            return null;
        }
    }

    private static class UpdateUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        private UpdateUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.updateUser(users[0]);
            return null;
        }
    }

    private static class DeleteUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        private DeleteUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.deleteUser(users[0]);
            return null;
        }
    }
}
