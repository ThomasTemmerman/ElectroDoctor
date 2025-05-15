package be.ucll.electrodoctor.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import be.ucll.electrodoctor.entity.User;
import com.google.common.util.concurrent.ListenableFuture;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM User WHERE userName = :userName")
    ListenableFuture<User> getUserByUsername(String userName);
    @Query("SELECT * FROM User")
    LiveData<List<User>> getAllUsers();
}
