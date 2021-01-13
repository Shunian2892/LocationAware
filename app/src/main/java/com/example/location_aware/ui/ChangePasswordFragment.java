package com.example.location_aware.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.location_aware.R;
import com.example.location_aware.data.Data;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * This class handles the password changes from a user. It takes the current firebase user, the new password from the screen, and then sets the new password in the database.
 */
public class ChangePasswordFragment extends Fragment {
    private FragmentManager fragmentManager;
    private SettingsFragmentButtons settingsFragmentButtons;
    private ImageButton ibBackButton;
    private Button confirm;
    private EditText newPass, repeatNewPass;
    private FirebaseUser user;
    private ProgressBar progressBar;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    public static ChangePasswordFragment newInstance(String param1, String param2) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_change_password, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();

        fragmentManager = getFragmentManager();
        ibBackButton = v.findViewById(R.id.change_password_backButton);
        settingsFragmentButtons = Data.getInstance().getSettingsButtons();

        confirm = v.findViewById(R.id.change_password_confirm);
        newPass = v.findViewById(R.id.change_password_newPass);
        repeatNewPass = v.findViewById(R.id.change_password_newPass_validate);
        progressBar = v.findViewById(R.id.change_password_progressBar);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Go back to settings menu
        ibBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction().show(settingsFragmentButtons).commit();
                fragmentManager.beginTransaction().hide(Data.getInstance().getChangePasswordFragment()).commit();
            }
        });

        //Confirm changing password
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String newPassword = newPass.getText().toString();
                String repeatNewPassword = repeatNewPass.getText().toString();

                //Check if the values of both edittexts are equal
                if(newPassword.equals(repeatNewPassword)){
                    //If both input strings are equal, update password for current user and show toast message
                    user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);
                                makeToast(R.string.toast_changePassword);
                                newPass.setText("");
                                repeatNewPass.setText("");
                            }
                        }
                    });
                } else {
                    //If both input strings aren't equal, then show user a toast message
                    makeToast(R.string.toast_changePassword_failed);
                }
            }
        });
    }

    /**
     * Make a new toast
     * @param message id of the string resource such that the text changes depending on the device language
     */
    private void makeToast(int message){
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

}