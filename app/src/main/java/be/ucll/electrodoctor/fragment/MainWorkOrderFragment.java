package be.ucll.electrodoctor.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import be.ucll.electrodoctor.AppDatabase;
import be.ucll.electrodoctor.R;
import be.ucll.electrodoctor.WorkOrderAdapter;
import be.ucll.electrodoctor.dao.UserDao;
import be.ucll.electrodoctor.entity.User;
import be.ucll.electrodoctor.entity.UserWithWorkOrder;
import be.ucll.electrodoctor.entity.WorkOrder;
import be.ucll.electrodoctor.viewModel.UserViewModel;
import be.ucll.electrodoctor.viewModel.WorkOrderViewModel;

public class MainWorkOrderFragment extends Fragment {

    private WorkOrderViewModel mViewModel;
    private UserViewModel mUserViewModel;

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
        this.mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
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

        //welkom bericht
        TextView txtWelcome = view.findViewById(R.id.txt_welcome);
        mUserViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                txtWelcome.setText("Welkom " + user.getUserName()+ "!");
            } else {
                txtWelcome.setText("Welkom gast!");
            }
        });
        //get all work orders
        mViewModel.getAllWorkOrders().observe(getViewLifecycleOwner(), workOrders -> {
            workOrderAdapter.setWorkOrders(workOrders);
        });
        //navigate to detailWorkOrder fragment
        workOrderAdapter.setOnWorkOrderClickListener(workOrder -> {
            Bundle bundle = new Bundle();
            boolean isProcessed = workOrder.getProcessed();
            //gives the ID of the workOrder to the detailWorkOrder fragment
            bundle.putLong("workOrderId", workOrder.getWorkOrderId());
            bundle.putString("DetailedProblemDescription", workOrder.getDetailedProblemDescription());
            bundle.putString("repairInformation", workOrder.getRepairInformation());
            //TODO: nog logica toevoegen om naar read-only of gewone fragment te gaan
            NavController navController = Navigation.findNavController(requireView());
            if (isProcessed){
                navController.navigate(R.id.action_mainWorkOrderFragment_to_detailWorkOrderFragmentReadOnly, bundle);
                Log.d("Processed","work order processed TRUE");
            } else {
                navController.navigate(R.id.action_mainWorkOrderFragment_to_detailWorkOrderFragment, bundle);
                Log.d("Processed","work order processed FALSE");
            }
        });
        //navigate to create workOrderFragment
        view.findViewById(R.id.toolbar_createbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_mainWorkOrderFragment_to_createWorkOrderFragment);
            }
        });
        return view;
    }
}