package be.ucll.electrodoctor.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import be.ucll.electrodoctor.AppDatabase;
import be.ucll.electrodoctor.dao.WorkOrderDao;
import be.ucll.electrodoctor.entity.User;
import be.ucll.electrodoctor.entity.UserWithWorkOrder;
import be.ucll.electrodoctor.entity.WorkOrder;

public class WorkOrderRepository {
    private final WorkOrderDao workOrderDao;
    private final LiveData<List<WorkOrder>> workOrders;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public WorkOrderRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getDatbase(application);
        workOrderDao = appDatabase.workOrderDao();
        workOrders = workOrderDao.getAllWorkOrders();
    }

    public LiveData<List<WorkOrder>> getAllWorkOrders() {
        return workOrders;
    }

    public void insert(WorkOrder workOrder) {
        executorService.execute(() -> {
            workOrderDao.insertWorkOrder(workOrder);
        });
    }
    public void delete(WorkOrder workOrder) {
        executorService.execute(() -> {
            workOrderDao.deleteWorkOrder(workOrder);
        });
    }
    public void update(WorkOrder workOrder) {
        executorService.execute(() -> {
            workOrderDao.updateWorkOrder(workOrder);
        });
    }
    public ListenableFuture<User> getUserByUsername(String userName) {
        return workOrderDao.getUserByUsername(userName);
    }
    public LiveData<List<UserWithWorkOrder>> getUserIdWithWorkOrders(long userId) {
        return workOrderDao.getUserIdWithWorkOrders(userId);
    }
}
