package be.ucll.electrodoctor.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

import be.ucll.electrodoctor.entity.User;
import be.ucll.electrodoctor.entity.UserWithWorkOrder;
import be.ucll.electrodoctor.entity.WorkOrder;
import be.ucll.electrodoctor.repository.WorkOrderRepository;

public class WorkOrderViewModel extends AndroidViewModel {
    private final WorkOrderRepository repo;
    private final LiveData<List<WorkOrder>> workOrders;
    public WorkOrderViewModel(@NonNull Application application) {
        super(application);
        repo = new WorkOrderRepository(application);
        workOrders = repo.getAllWorkOrders();
    }
    public LiveData<List<WorkOrder>> getAllWorkOrders() {
        return workOrders;
    }
    public void Insert(WorkOrder workOrder) {
        repo.insert(workOrder);
    }
    public void Delete(WorkOrder workOrder) {
        repo.delete(workOrder);
    }
    public void Update(WorkOrder workOrder) {
        repo.update(workOrder);
    }
    public ListenableFuture<User> getUserByUsername(String userName) {
        return repo.getUserByUsername(userName);
    }
    public LiveData<List<UserWithWorkOrder>> getUserIdWithWorkOrders(long userId) {
        return repo.getUserIdWithWorkOrders(userId);
    }
}
