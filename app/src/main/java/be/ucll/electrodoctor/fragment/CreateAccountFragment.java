package be.ucll.electrodoctor.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import be.ucll.electrodoctor.R;
import be.ucll.electrodoctor.entity.User;
import be.ucll.electrodoctor.viewmodel.UserViewModel;

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
        view.findViewById(R.id.createButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO create account
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
                                //validaties toevoegen!
                                User user = new User();
                                try {
                                    Date birthday = sdf.parse(date.getText().toString());
                                    user.setBirthdate(birthday);
                                } catch (ParseException e) {
                                    Toast.makeText(getContext(), "Invalid date format", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (firstname.getText().toString().isEmpty() || lastname.getText().toString().isEmpty()
                                        || date.getText().toString().isEmpty() || municipality.getText().toString().isEmpty()
                                        || postalcode.getText().toString().isEmpty() || street.getText().toString().isEmpty()
                                        || house.getText().toString().isEmpty() || username.getText().toString().isEmpty()
                                        || password.getText().toString().isEmpty()) {
                                    Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
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
                                mViewModel.insert(user);

                                NavController navController = Navigation.findNavController(view);
                                navController.navigate(R.id.action_createaccountFragment_to_loginFragment);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Error creating account", Toast.LENGTH_SHORT).show();
                            }
                        });
                }, Executors.newSingleThreadExecutor());
            }
        });
        return view;
    }
}