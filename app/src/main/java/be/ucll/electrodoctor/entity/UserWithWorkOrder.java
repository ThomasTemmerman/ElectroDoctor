package be.ucll.electrodoctor.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class UserWithWorkOrder {
    @Embedded public User user;
    @Relation(
            parentColumn = "userId",
            entityColumn = "userId"
    )
    public List<WorkOrder> workOrders;
}
