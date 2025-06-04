package be.ucll.electrodoctor.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import be.ucll.electrodoctor.NavigationOrigin;
import be.ucll.electrodoctor.R;
import be.ucll.electrodoctor.adapter.WorkOrderAdapter;
import be.ucll.electrodoctor.entity.UserWithWorkOrder;
import be.ucll.electrodoctor.entity.WorkOrder;
import be.ucll.electrodoctor.viewModel.UserViewModel;
import be.ucll.electrodoctor.viewModel.WorkOrderViewModel;

public class MainWorkOrderFragment extends Fragment {

    private WorkOrderViewModel mWorkOrderViewModel;
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
        this.mWorkOrderViewModel = new ViewModelProvider(this).get(WorkOrderViewModel.class);
        this.mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_work_order, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.workorder_list);
        WorkOrderAdapter workOrderAdapter = new WorkOrderAdapter();
        TextView txtWelcome = view.findViewById(R.id.txt_welcome);

        recyclerView.setAdapter(workOrderAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //get all work orders by current user
        mUserViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
                    if (user != null) {
                        Log.d("DEBUG", "Current user found: " + user.getUserName() + " (ID: " + user.getUserId() + ")");
                        //welcome message
                        txtWelcome.setText("Welkom " + user.getUserName()+ "!");//of first- en lastname

                        mUserViewModel.getCurrentUserWithWorkOrders().observe(getViewLifecycleOwner(), userWithWorkOrdersList -> {
                            Log.d("DEBUG", "UserWithWorkOrders callback triggered");

                            if (userWithWorkOrdersList != null && !userWithWorkOrdersList.isEmpty()) {
                                UserWithWorkOrder userWithWorkOrders = userWithWorkOrdersList.get(0);
                                List<WorkOrder> workOrders = userWithWorkOrders.workOrders;
                                Log.d("DEBUG", "Work orders for user: " + (workOrders != null ? workOrders.size() : "null"));
                                workOrderAdapter.setWorkOrders(workOrders);
                            } else {
                                Log.d("DEBUG", "UserWithWorkOrdersList is empty");
                            }
                        });
                } else {
                        txtWelcome.setText("Welkom gast!");
                        Log.d("DEBUG", "No current user found");
                    }
                });

        workOrderAdapter.setOnWorkOrderClickListener(workOrder -> {
            Bundle bundle = new Bundle();
            boolean isProcessed = workOrder.getProcessed();
            //gives the ID of the workOrder to the detailWorkOrder fragment
            bundle.putLong("workOrderId", workOrder.getWorkOrderId());
            bundle.putString("DetailedProblemDescription", workOrder.getDetailedProblemDescription());
            bundle.putString("repairInformation", workOrder.getRepairInformation());
            bundle.putSerializable("navigationOrigin",NavigationOrigin.MAIN.name());
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
        Toolbar toolbar = view.findViewById(R.id.toolbar);

        // Menu handling
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_home) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.mainWorkOrderFragment);
                return true;
            } else if (item.getItemId() == R.id.action_logout) {
                // Logout logica
                NavOptions navOptions = new NavOptions.Builder()
                        .setEnterAnim(R.anim.slide_in_from_top)
                        .setExitAnim(R.anim.fade_out)
                        .build();
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.loginFragment,null,navOptions);
                return true;
            }
            return false;
        });
        return view;
    }
}