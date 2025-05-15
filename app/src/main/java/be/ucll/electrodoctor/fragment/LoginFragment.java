package be.ucll.electrodoctor.fragment;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.Executors;

import be.ucll.electrodoctor.R;
import be.ucll.electrodoctor.entity.User;
import be.ucll.electrodoctor.viewmodel.UserViewModel;

public class LoginFragment extends Fragment {

    private UserViewModel mViewModel;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        view.findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = view.findViewById(R.id.editUsername);
                EditText password = view.findViewById(R.id.editPassword);
                TextView errorText = view.findViewById(R.id.textError);
                ListenableFuture<User> userFuture = mViewModel.getUserByUsername(username.getText().toString());

                userFuture.addListener(() -> {
                    try {
                        User user = userFuture.get();
                        if (user != null) {
                            if (user.getPassword().equals(password.getText().toString()) && user.getUserName().equals(username.getText().toString())) {
                                requireActivity().runOnUiThread(() -> {
                                    Log.d("LoginFragment", "Login successful");
                                    errorText.setTextColor(Color.parseColor("#a4c639"));
                                    errorText.setText("Login succesful!");

                                    //delay van 3sec
                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            NavController navController = Navigation.findNavController(view);
                                            navController.navigate(R.id.action_loginFragment_to_testFragment);
                                        }
                                    }, 3000);
                                });
                            } else {
                                //TODO field text?
                                requireActivity().runOnUiThread(() -> {
                                    //Toast.makeText(getContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                                    errorText.setTextColor(Color.parseColor("#FF0000"));
                                    //nog tijd toevoegen
                                    errorText.setText("Wrong password!");
                                });
                            }
                        } else {
                            requireActivity().runOnUiThread(() -> {
                                //Toast.makeText(getContext(), "User or password incorrect", Toast.LENGTH_SHORT).show();
                                errorText.setTextColor(Color.parseColor("#FF0000"));
                                //nog tijd toevoegen
                                errorText.setText("User or password incorrect!");
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, Executors.newSingleThreadExecutor());
            }
        });
        return view;
    }
}
