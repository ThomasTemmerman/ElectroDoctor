package be.ucll.electrodoctor.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.common.util.concurrent.ListenableFuture;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import be.ucll.electrodoctor.entity.User;
import be.ucll.electrodoctor.entity.UserWithWorkOrder;
import be.ucll.electrodoctor.entity.WorkOrder;
import be.ucll.electrodoctor.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {
    private UserRepository repo;
    private LiveData<List<User>> users;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repo = new UserRepository(application);
        users = repo.getAllUsers();
    }
    public LiveData<List<User>> getAllUsers() {
        return users;
    }
    public void insert(User user) {
        repo.insert(user);
    }
    public ListenableFuture<User> getUserByUsername(String username) {
        return repo.getUserByUserName(username);
    }
    public Long insertWithId(User user) {
        return repo.insertWithId(user);
    }
    //FK
    public LiveData<List<UserWithWorkOrder>> getUsersWithWorkOrders() {
        return repo.getUsersWithWorkOrders();
    }
    public LiveData<User> getCurrentUser() {
        return repo.getCurrentUser();
    }
    public void setCurrentUser(String username) {
        repo.setCurrentUser(username);
    }
    public LiveData<List<UserWithWorkOrder>> getCurrentUserWithWorkOrders() {
        return repo.getCurrentUserWithWorkOrders();
    }
    public LiveData<List<UserWithWorkOrder>> getUserWithDetails() {
        return repo.getUserWithDetails();
    }
    public void loginAndSetCurrentUser(String username) {
        repo.makeUserCurrent(username);
    }
    public void clearOtherCurrentUsers(String newCurrentUsername) {
        repo.clearOtherCurrentUsers(newCurrentUsername);
    }
    public void createAndSetCurrentUser(User user) {
        repo.createAndSetCurrentUser(user);
    }
}
