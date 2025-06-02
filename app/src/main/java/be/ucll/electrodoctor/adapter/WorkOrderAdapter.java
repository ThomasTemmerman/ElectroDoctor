package be.ucll.electrodoctor.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import be.ucll.electrodoctor.R;
import be.ucll.electrodoctor.entity.WorkOrder;

//info: https://developer.android.com/develop/ui/views/layout/recyclerview
public class WorkOrderAdapter extends RecyclerView.Adapter<WorkOrderAdapter.WorkOrderViewHolder> {
    private List<WorkOrder> workOrders = new ArrayList<>();
    public onWorkOrderClickListener clickListener;

    public interface onWorkOrderClickListener {
        void onWorkOrderClick(WorkOrder workOrder);
    }
    public WorkOrderAdapter() {
        this.clickListener = null;
    }
    public void setOnWorkOrderClickListener(onWorkOrderClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setWorkOrders(List<WorkOrder> newOrders) {
        workOrders = newOrders;
        notifyDataSetChanged();
    }
    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public WorkOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workorder, parent, false);
        return new WorkOrderViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull WorkOrderAdapter.WorkOrderViewHolder holder, int position) {
        WorkOrder wo = workOrders.get(position);
        holder.city.setText(wo.getCity());
        holder.device.setText(wo.getDevice());
        holder.code.setText(wo.getProblemCode());
        holder.name.setText(wo.getCustomerName());
        holder.processed.setChecked(wo.getProcessed());

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onWorkOrderClick(wo);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return workOrders.size();
    }

    static class WorkOrderViewHolder extends RecyclerView.ViewHolder {
        TextView city, device, code, name;
        CheckBox processed;

        public WorkOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            city = itemView.findViewById(R.id.txt_city);
            device = itemView.findViewById(R.id.txt_device);
            code = itemView.findViewById(R.id.txt_code);
            name = itemView.findViewById(R.id.txt_name);
            processed = itemView.findViewById(R.id.chck_processed);
        }
    }
}
