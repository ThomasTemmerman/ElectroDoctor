package be.ucll.electrodoctor.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

import be.ucll.electrodoctor.entity.User;
import be.ucll.electrodoctor.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {
    //uitbreiding van WorkOrder?
    private UserRepository repo;
    private LiveData<List<User>> users;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repo = new UserRepository(application);
        //users = repo.getAllUsers();
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
}
