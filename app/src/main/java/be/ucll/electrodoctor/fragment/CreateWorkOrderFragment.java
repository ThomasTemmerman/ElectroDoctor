package be.ucll.electrodoctor.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import be.ucll.electrodoctor.EditTextClear;
import be.ucll.electrodoctor.R;
import be.ucll.electrodoctor.entity.WorkOrder;
import be.ucll.electrodoctor.viewModel.UserViewModel;
import be.ucll.electrodoctor.viewModel.WorkOrderViewModel;

public class CreateWorkOrderFragment extends Fragment {

    private WorkOrderViewModel mViewModel;
    private UserViewModel mUserViewModel;

    public CreateWorkOrderFragment() {
        // Required empty public constructor
    }

    public static CreateWorkOrderFragment newInstance(String param1, String param2) {
        return new CreateWorkOrderFragment();
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
        View view = inflater.inflate(R.layout.fragment_create_work_order, container, false);
        EditText city = view.findViewById(R.id.editCity);
        EditTextClear.enableClearButton(city, getContext(), R.drawable.ic_clear);
        EditText code = view.findViewById(R.id.editCode);
        EditTextClear.enableClearButton(code, getContext(), R.drawable.ic_clear);
        EditText device = view.findViewById(R.id.editDevice);
        EditTextClear.enableClearButton(device, getContext(), R.drawable.ic_clear);
        EditText name = view.findViewById(R.id.editName);
        EditTextClear.enableClearButton(name, getContext(), R.drawable.ic_clear);
        TextView errorText = view.findViewById(R.id.errorText);

        view.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                city.setText("");
                code.setText("");
                device.setText("");
                name.setText("");
            }
        });
        view.findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserViewModel.getCurrentUser().observe(getViewLifecycleOwner(), currentUser -> {
                    if (currentUser != null) {
                        WorkOrder workOrder = new WorkOrder();
                        workOrder.setCity(city.getText().toString());
                        workOrder.setProblemCode(code.getText().toString());
                        workOrder.setDevice(device.getText().toString());
                        workOrder.setCustomerName(name.getText().toString());
                        workOrder.setProcessed(false);

                        workOrder.setUserId(currentUser.getUserId());//bug fixed!

                        mViewModel.checkIfWorkOrderExists(workOrder, exists -> {
                            if (exists) {
                                Snackbar snackbar = Snackbar.make(view, "Work order already exists!", Snackbar.LENGTH_LONG);
                                snackbar.setBackgroundTint(ContextCompat.getColor(view.getContext(), android.R.color.holo_red_dark));
                                snackbar.show();
                                errorText.setTextColor(Color.parseColor("#FF0000"));
                                errorText.setText("Not saved! Work order with: " + workOrder.getCity() + ", " + workOrder.getDevice() + ", " + workOrder.getCustomerName() + " already exists!");
                            } else if (city.getText().toString().isEmpty() || code.getText().toString().isEmpty() || device.getText().toString().isEmpty() || name.getText().toString().isEmpty()) {
                                {
                                    Snackbar snackbar = Snackbar.make(view, "Please fill in all fields!", Snackbar.LENGTH_LONG);
                                    snackbar.setBackgroundTint(ContextCompat.getColor(view.getContext(), android.R.color.holo_red_dark));
                                    snackbar.show();
                                }
                            } else {
                                Log.d("Work order created", workOrder.toString());
                                mViewModel.insert(workOrder);
                                Snackbar snackbar = Snackbar.make(view, "Work order created!", Snackbar.LENGTH_LONG);
                                snackbar.setBackgroundTint(ContextCompat.getColor(view.getContext(), android.R.color.holo_green_dark));
                                snackbar.show();
                                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                    NavController navController = Navigation.findNavController(view);
                                    navController.navigate(R.id.action_createWorkOrderFragment_to_mainWorkOrderFragment);
                                    }, 3000);
                            }
                        });
                    } else {
                        Snackbar snackbar = Snackbar.make(view, "No current user!", Snackbar.LENGTH_LONG);
                        snackbar.setBackgroundTint(ContextCompat.getColor(view.getContext(), android.R.color.holo_red_dark));
                        snackbar.show();
                    }
                });
            }
        });
        view.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_createWorkOrderFragment_to_mainWorkOrderFragment);
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