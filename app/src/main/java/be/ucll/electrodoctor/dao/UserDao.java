package be.ucll.electrodoctor.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import be.ucll.electrodoctor.entity.User;
import be.ucll.electrodoctor.entity.UserWithWorkOrder;
import be.ucll.electrodoctor.entity.WorkOrder;

import com.google.common.util.concurrent.ListenableFuture;

import org.checkerframework.checker.units.qual.A;

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

    @Query("SELECT * FROM User WHERE isCurrentUser = 1 LIMIT 1")
    LiveData<User> getCurrentUser();
    @Query("UPDATE User SET isCurrentUser = 1 WHERE userName = :username")
    void setCurrentUser(String username);

    @Transaction
    @Query("SELECT * FROM User WHERE isCurrentUser = 1 LIMIT 1")
    LiveData<List<UserWithWorkOrder>> getCurrentUserWithWorkOrders();

    @Transaction
    @Query("SELECT * FROM User WHERE isCurrentUser = 1 LIMIT 1")
    LiveData<List<UserWithWorkOrder>> getUserWithDetails();

    @Transaction
    default void makeUserCurrent(String username) {
        clearOtherCurrentUsers(username);
        setCurrentUser(username);
    }

    @Query("UPDATE User SET isCurrentUser = 0 WHERE isCurrentUser = 1 AND userName != :newCurrentUsername")
    void clearOtherCurrentUsers(String newCurrentUsername);

    @Transaction
    default void createAndSetCurrentUser(User user) {
        makeUserCurrent(user.getUserName());
        insertUser(user);
    }
}
