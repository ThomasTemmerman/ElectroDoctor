package be.ucll.electrodoctor.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

import be.ucll.electrodoctor.entity.User;
import be.ucll.electrodoctor.entity.UserWithWorkOrder;
import be.ucll.electrodoctor.entity.WorkOrder;

@Dao
public interface WorkOrderDao {
    @Insert
    void insertWorkOrder(WorkOrder workOrder);
    @Delete
    void deleteWorkOrder(WorkOrder workOrder);
    @Update
    void updateWorkOrder(WorkOrder workOrder);

    @Query("SELECT * FROM WorkOrder")
    LiveData<List<WorkOrder>> getAllWorkOrders();

    @Transaction
    @Query("SELECT * FROM User WHERE userName = :userId")
    LiveData<List<UserWithWorkOrder>> getUserIdWithWorkOrders(long userId);

    @Query("SELECT * FROM User WHERE userName = :userName")
    ListenableFuture<User> getUserByUsername(String userName);

}
