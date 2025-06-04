package be.ucll.electrodoctor.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import be.ucll.electrodoctor.NavigationOrigin;
import be.ucll.electrodoctor.R;
import be.ucll.electrodoctor.entity.WorkOrder;
import be.ucll.electrodoctor.viewModel.WorkOrderViewModel;

public class ReadOnlyDetailWorkOrderFragment extends Fragment {
    private WorkOrderViewModel mWorkOrderViewModel;
    private WorkOrder workOrder;

    public ReadOnlyDetailWorkOrderFragment() {
        // Required empty public constructor
    }

    public static ReadOnlyDetailWorkOrderFragment newInstance(String param1, String param2) {
        return new ReadOnlyDetailWorkOrderFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mWorkOrderViewModel = new ViewModelProvider(this).get(WorkOrderViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_work_order_readonly, container, false);
        TextView txtDescription = view.findViewById(R.id.textViewDescription);
        TextView txtRepairInformation = view.findViewById(R.id.TextRepairInformation);

        Bundle bundle = getArguments();
        if (bundle != null) {
            long workOrderId = bundle.getLong("workOrderId", -1);
            String Description = bundle.getString("DetailedProblemDescription");
            txtDescription.setText(Description);

            if (workOrderId != -1) {
                mWorkOrderViewModel.getWorkOrderById(workOrderId).observe(getViewLifecycleOwner(), w -> {
                    if (w != null) {
                        workOrder = w;
                        txtRepairInformation.setText(workOrder.getRepairInformation());
                    }
                });
            }
        }
        //reopen button
        view.findViewById(R.id.reOpen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (workOrder != null) {
                    Bundle readonlyBundle = new Bundle();
                    readonlyBundle.putLong("workOrderId", workOrder.getWorkOrderId());
                    readonlyBundle.putString("DetailedProblemDescription", workOrder.getDetailedProblemDescription());
                    readonlyBundle.putString("repairInformation", workOrder.getRepairInformation());
                    readonlyBundle.putSerializable("navigationOrigin",NavigationOrigin.READ_ONLY.name());

                    NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.action_detailWorkOrderFragmentReadOnly_to_detailWorkOrderFragment, readonlyBundle);
                }
            }
        });
        Toolbar toolbar = view.findViewById(R.id.toolbar);

        //Menu handling
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_home) {
                //Navigate to HomeFragment
                NavOptions navOptions = new NavOptions.Builder()
                        .setEnterAnim(R.anim.zoom_in_enter)
                        .setExitAnim(R.anim.fade_out)
                        .build();
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.mainWorkOrderFragment,null,navOptions);
                return true;
            } else if (item.getItemId() == R.id.action_logout) {
                //Navigate to LoginFragment
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