package be.ucll.electrodoctor.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import be.ucll.electrodoctor.AppDatabase;
import be.ucll.electrodoctor.dao.UserDao;
import be.ucll.electrodoctor.dao.WorkOrderDao;
import be.ucll.electrodoctor.entity.User;
import be.ucll.electrodoctor.entity.UserWithWorkOrder;
import be.ucll.electrodoctor.entity.WorkOrder;

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
    public LiveData<User> getCurrentUser() {
        return userDao.getCurrentUser();
    }
    public void setCurrentUser(String username) {
        executorService.execute(() -> {
            userDao.setCurrentUser(username);
        });
        }
        public LiveData<List<UserWithWorkOrder>> getCurrentUserWithWorkOrders() {
            return userDao.getCurrentUserWithWorkOrders();
        }
        public LiveData<List<UserWithWorkOrder>> getUserWithDetails() {
            return userDao.getUserWithDetails();
        }
        public void makeUserCurrent(String username) {
            executorService.execute(() -> {
                userDao.makeUserCurrent(username);
            });
        }
}
