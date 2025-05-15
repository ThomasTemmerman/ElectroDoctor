package be.ucll.electrodoctor.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import be.ucll.electrodoctor.AppDatabase;
import be.ucll.electrodoctor.dao.UserDao;
import be.ucll.electrodoctor.entity.User;

public class UserRepository {
    private UserDao userDao;
    private LiveData<List<User>> users;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();//AsyncTask == deprecated

    public UserRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getDatbase(application);
        userDao = appDatabase.userDao();
        users = userDao.getAllUsers();
    }

    public LiveData<List<User>> getAllUsers() {
        return users;
    }
    public ListenableFuture<User> getUserByUserName(String username) {
        return userDao.getUserByUsername(username);
    }
    public void insert(User user) {
        executorService.execute(() -> {
            userDao.insertUser(user);
        });
    }
    //eventueel nog andere CRUD
}
