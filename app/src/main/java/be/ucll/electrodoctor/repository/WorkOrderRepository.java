package be.ucll.electrodoctor.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import be.ucll.electrodoctor.core.AppDatabase;
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
    public interface WorkOrderCallback {
        void onResult(boolean exists);
    }

    //Moet dit doen omdat Room niet toelaat op main thread
    public void checkIfWorkOrderExists(WorkOrder workOrder, Consumer<Boolean> callback) {
        executorService.execute(() -> {
            WorkOrder result = workOrderDao.findByCityDeviceAndCustomerName(
                    workOrder.getCity(),
                    workOrder.getDevice(),
                    workOrder.getCustomerName()
            );
            boolean exists = (result != null);

            // Terug naar main thread voor UI interactie
            new Handler(Looper.getMainLooper()).post(() -> {
                callback.accept(exists);
            });
        });
    }
    public LiveData<WorkOrder> getWorkOrderById(long workOrderId) {
        return workOrderDao.getWorkOrderById(workOrderId);
    }
    public LiveData<List<WorkOrder>> getWorkOrdersByUserId(long userId) {
        return workOrderDao.getWorkOrdersByUserId(userId);
    }
}
