package be.ucll.electrodoctor.fragment;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.common.util.concurrent.ListenableFuture;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

import be.ucll.electrodoctor.R;
import be.ucll.electrodoctor.entity.User;
import be.ucll.electrodoctor.viewModel.UserViewModel;

public class CreateAccountFragment extends Fragment {

    private UserViewModel mViewModel;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public CreateAccountFragment() {
        // Required empty public constructor
    }
    public static CreateAccountFragment newInstance() {
        return new CreateAccountFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_account, container, false);
        //link klikbaar en button klikbaar maken op basis van checkbox
        TextView tosView = view.findViewById(R.id.tosTextView);
        Button button = view.findViewById(R.id.createButton);
        tosView.setMovementMethod(LinkMovementMethod.getInstance());
        CheckBox checkBox = view.findViewById(R.id.radioButton);
        button.setEnabled(checkBox.isChecked());
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            button.setEnabled(isChecked);
        });

        view.findViewById(R.id.createButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //View binding?
                EditText firstname = view.findViewById(R.id.editFirstname);
                EditText lastname = view.findViewById(R.id.editLastname);
                EditText date = view.findViewById(R.id.editDate);
                EditText municipality = view.findViewById(R.id.editMunicipality);
                EditText postalcode = view.findViewById(R.id.editPostalcode);
                EditText street = view.findViewById(R.id.editStreet);
                EditText house = view.findViewById(R.id.editHouse);
                EditText box = view.findViewById(R.id.editBox);
                EditText username = view.findViewById(R.id.editCreateUsername);
                EditText password = view.findViewById(R.id.editCreatePassword);
                ListenableFuture<User> userFuture = mViewModel.getUserByUsername(username.getText().toString());

                userFuture.addListener(() -> {
                        requireActivity().runOnUiThread(() -> {
                            try {
                                User existingUser = userFuture.get(); // Get the result of the query

                                if (existingUser != null) {
                                    Snackbar snackbar = Snackbar.make(view, "Username already exists!", Snackbar.LENGTH_LONG);
                                    snackbar.setBackgroundTint(ContextCompat.getColor(view.getContext(), android.R.color.holo_red_dark));
                                    snackbar.show();
                                    return;
                                }

                                User user = new User();
                                try {
                                    Date birthday = sdf.parse(date.getText().toString());
                                    user.setBirthdate(birthday);
                                } catch (ParseException e) {
                                    Snackbar snackbar = Snackbar.make(view, "Invalid date format!", Snackbar.LENGTH_LONG);
                                    snackbar.setBackgroundTint(ContextCompat.getColor(view.getContext(), android.R.color.holo_red_dark));
                                    snackbar.show();
                                    return;
                                }

                                if (firstname.getText().toString().isEmpty() || lastname.getText().toString().isEmpty()
                                        || date.getText().toString().isEmpty() || postalcode.getText().toString().isEmpty()
                                        || street.getText().toString().isEmpty() || house.getText().toString().isEmpty()
                                        || username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                                    Snackbar snackbar = Snackbar.make(view, "Please fill in all fields!", Snackbar.LENGTH_LONG);
                                    snackbar.setBackgroundTint(ContextCompat.getColor(view.getContext(), android.R.color.holo_red_dark));
                                    snackbar.show();
                                    return;
                                }

                                user.setFirstName(firstname.getText().toString());
                                user.setLastName(lastname.getText().toString());
                                user.setMunicipality(municipality.getText().toString());
                                user.setPostalcode(postalcode.getText().toString());
                                user.setStreet(street.getText().toString());
                                user.setHouseNumber(house.getText().toString());
                                user.setBox(box.getText().toString());
                                user.setUserName(username.getText().toString());
                                user.setPassword(password.getText().toString());

                                mViewModel.createAndSetCurrentUser(user);

                                Snackbar snackbar = Snackbar.make(view, "Account created!", Snackbar.LENGTH_LONG);
                                snackbar.setBackgroundTint(ContextCompat.getColor(view.getContext(), android.R.color.holo_green_dark));
                                snackbar.show();

                                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                    NavController navController = Navigation.findNavController(view);
                                    navController.navigate(R.id.action_createaccountFragment_to_loginFragment);
                                }, 3000);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Snackbar snackbar = Snackbar.make(view, "Error creating account", Snackbar.LENGTH_LONG);
                                snackbar.setBackgroundTint(ContextCompat.getColor(view.getContext(), android.R.color.holo_red_dark));
                                snackbar.show();
                            }
                        });
                }, Executors.newSingleThreadExecutor());
            }
        });
        return view;
    }
}