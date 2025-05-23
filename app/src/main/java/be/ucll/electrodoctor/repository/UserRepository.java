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
import be.ucll.electrodoctor.entity.UserWithWorkOrder;

public class UserRepository {
    private final UserDao userDao;
    private final LiveData<List<User>> users;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();//AsyncTask == deprecated

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
    public Long insertWithId(User user) {
        return userDao.insertUserWithId(user);
    }
    public LiveData<List<UserWithWorkOrder>> getUsersWithWorkOrders() {
        return userDao.getUsersWithWorkOrders();
    }
}
