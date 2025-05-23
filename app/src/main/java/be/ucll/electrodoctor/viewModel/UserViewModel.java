package be.ucll.electrodoctor.viewmodel;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.common.util.concurrent.ListenableFuture;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

import be.ucll.electrodoctor.entity.User;
import be.ucll.electrodoctor.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {
    //uitbreiding van WorkOrder?
    private UserRepository repo;
    private LiveData<List<User>> users;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

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
}
