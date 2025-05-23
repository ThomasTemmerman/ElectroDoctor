package be.ucll.electrodoctor.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import be.ucll.electrodoctor.R;
import be.ucll.electrodoctor.WorkOrderAdapter;
import be.ucll.electrodoctor.entity.UserWithWorkOrder;
import be.ucll.electrodoctor.entity.WorkOrder;
import be.ucll.electrodoctor.viewModel.WorkOrderViewModel;

public class MainWorkOrderFragment extends Fragment {

    private WorkOrderViewModel mViewModel;

    public MainWorkOrderFragment() {
        // Required empty public constructor
    }
    public static MainWorkOrderFragment newInstance() {
        return new MainWorkOrderFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mViewModel = new ViewModelProvider(this).get(WorkOrderViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_work_order, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.workorder_list);
        WorkOrderAdapter workOrderAdapter = new WorkOrderAdapter();
        recyclerView.setAdapter(workOrderAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mViewModel.getAllWorkOrders().observe(getViewLifecycleOwner(), workOrders -> {
            workOrderAdapter.setWorkOrders(workOrders);
        });
        return view;
    }
}