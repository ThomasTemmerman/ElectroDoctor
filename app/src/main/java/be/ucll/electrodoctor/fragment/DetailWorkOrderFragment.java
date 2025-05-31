package be.ucll.electrodoctor.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import be.ucll.electrodoctor.R;
import be.ucll.electrodoctor.entity.WorkOrder;
import be.ucll.electrodoctor.viewModel.WorkOrderViewModel;

public class DetailWorkOrderFragment extends Fragment {
    private WorkOrderViewModel mWorkOrderViewModel;
    private WorkOrder currentWorkOrder;

    public DetailWorkOrderFragment() {
        // Required empty public constructor
    }

    public static DetailWorkOrderFragment newInstance(String param1, String param2) {
        return new DetailWorkOrderFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mWorkOrderViewModel = new ViewModelProvider(this).get(WorkOrderViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_work_order, container, false);
        TextView txtDescription = view.findViewById(R.id.textViewDescription);
        EditText txtRepairInformation = view.findViewById(R.id.editTextRepairInformation);
        TextView txtError = view.findViewById(R.id.textViewErrorDetailed);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String Description = bundle.getString("DetailedProblemDescription");
            txtDescription.setText(Description);
            //workOrderId ophalen uit bundle
            long workOrderId = bundle.getLong("workOrderId", -1);

            if (workOrderId != -1) {
                mWorkOrderViewModel.getWorkOrderById(workOrderId).observe(getViewLifecycleOwner(), workOrder -> {
                    if (workOrder != null) {
                        this.currentWorkOrder = workOrder;
                        //checken
                        txtRepairInformation.setText(workOrder.getRepairInformation());
                    }
                });
            }
            view.findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentWorkOrder != null) {
                        String repairInformation = txtRepairInformation.getText().toString();

                        if (!repairInformation.isEmpty()){
                            currentWorkOrder.setRepairInformation(repairInformation);
                            currentWorkOrder.setProcessed(true);
                            mWorkOrderViewModel.update(currentWorkOrder);
                            txtError.setTextColor(Color.parseColor("#a4c639"));
                            txtError.setText("Work order saved!");
                            Snackbar snackbar = Snackbar.make(view, "Work order saved!", Snackbar.LENGTH_LONG);
                            snackbar.setBackgroundTint(ContextCompat.getColor(view.getContext(), android.R.color.holo_green_dark));
                            snackbar.show();
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            NavController navController = Navigation.findNavController(view);
                            navController.navigate(R.id.action_detailWorkOrderFragment_to_mainWorkOrderFragment);
                            }, 3000);
                    } else {
                        txtError.setTextColor(Color.parseColor("#FF0000"));
                        txtError.setText("Not saved. No repair information was enterred!");
                        }
                    } else {
                        txtError.setTextColor(Color.parseColor("#FF0000"));
                        txtError.setText("Not saved. No work order found!");
                    }
                }
            });
            view.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.action_detailWorkOrderFragment_to_mainWorkOrderFragment);
                }
            });
        }
        return view;
    }
}