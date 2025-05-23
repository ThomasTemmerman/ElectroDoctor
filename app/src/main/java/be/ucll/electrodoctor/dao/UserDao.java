package be.ucll.electrodoctor.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import be.ucll.electrodoctor.entity.User;
import be.ucll.electrodoctor.entity.UserWithWorkOrder;

import com.google.common.util.concurrent.ListenableFuture;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User user);

    @Insert
    Long insertUserWithId(User user);

    @Query("SELECT * FROM User WHERE userName = :userName")
    ListenableFuture<User> getUserByUsername(String userName);

    @Query("SELECT * FROM User")
    LiveData<List<User>> getAllUsers();

    @Transaction
    @Query("SELECT * FROM User")
    LiveData<List<UserWithWorkOrder>> getUsersWithWorkOrders();
}
