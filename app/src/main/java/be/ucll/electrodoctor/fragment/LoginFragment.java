package be.ucll.electrodoctor.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.Executors;

import be.ucll.electrodoctor.EditTextClear;
import be.ucll.electrodoctor.R;
import be.ucll.electrodoctor.entity.User;
import be.ucll.electrodoctor.viewModel.UserViewModel;

    public class LoginFragment extends Fragment {

    private UserViewModel mViewModel;

    public LoginFragment() {
        // Required empty public constructor
    }
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        EditText username = view.findViewById(R.id.editUsername);
        EditTextClear.enableClearButton(username, getContext(), R.drawable.ic_clear);
        EditText password = view.findViewById(R.id.editPassword);
        EditTextClear.enableClearButton(password, getContext(), R.drawable.ic_clear);
        TextView errorText = view.findViewById(R.id.textError);

        view.findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListenableFuture<User> userFuture = mViewModel.getUserByUsername(username.getText().toString());

                userFuture.addListener(() -> {
                    try {
                        User user = userFuture.get();
                        if (user != null) {
                            if (user.getPassword().equals(password.getText().toString()) && user.getUserName().equals(username.getText().toString())) {
                                requireActivity().runOnUiThread(() -> {
                                    mViewModel.clearOtherCurrentUsers(user.getUserName());
                                    mViewModel.setCurrentUser(user.getUserName());//user meepakken naar main fragment
                                    Log.d("LoginFragment", "Login successful");
                                    errorText.setTextColor(Color.parseColor("#a4c639"));
                                    errorText.setText("Login succesful!");
                                    Snackbar snackbar = Snackbar.make(view, "Welcome " + user.getUserName() + " to ElectroMan!",Snackbar.LENGTH_LONG);
                                    snackbar.setBackgroundTint(ContextCompat.getColor(view.getContext(), android.R.color.holo_green_dark));
                                    snackbar.show();

                                    //delay van 3sec
                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            NavController navController = Navigation.findNavController(view);
                                            navController.navigate(R.id.action_loginFragment_to_MainWorkOrderFragment);
                                        }
                                    }, 3000);
                                });
                            } else {
                                requireActivity().runOnUiThread(() -> {
                                    //Toast.makeText(getContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                                    errorText.setTextColor(Color.parseColor("#FF0000"));
                                    errorText.setText("Wrong password!");
                                    Log.d("LoginFragment", "Wrong password error received!");
                                });
                            }
                        } else {
                            requireActivity().runOnUiThread(() -> {
                                //Toast.makeText(getContext(), "User or password incorrect", Toast.LENGTH_SHORT).show();
                                errorText.setTextColor(Color.parseColor("#FF0000"));
                                errorText.setText("User or password is incorrect!");
                                Log.d("LoginFragment", "User or password is incorrect error received!");
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, Executors.newSingleThreadExecutor());
            }
        });
        view.findViewById(R.id.accountButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_loginFragment_to_createaccountFragment);
                Log.d("LoginFragment", "Navigate to create account succes!");
            }
        });
        return view;
    }
}
