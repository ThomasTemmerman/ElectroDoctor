package be.ucll.electrodoctor.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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
        view.findViewById(R.id.reOpen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle readonlyBundle = new Bundle();
                readonlyBundle.putLong("workOrderId",workOrder.getWorkOrderId());
                readonlyBundle.putString("DetailedProblemDescription", workOrder.getDetailedProblemDescription());
                readonlyBundle.putString("repairInformation", workOrder.getRepairInformation());
                    NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.action_detailWorkOrderFragmentReadOnly_to_detailWorkOrderFragment,readonlyBundle);
            }
        });
        view.findViewById(R.id.action_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }
}