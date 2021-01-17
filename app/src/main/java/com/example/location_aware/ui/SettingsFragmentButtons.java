package com.example.location_aware.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.location_aware.R;
import com.example.location_aware.data.Data;
import com.google.firebase.auth.FirebaseAuth;

/**
 * This class is the settings fragment with the different setting options: log out, create own route, and change password
 */
public class SettingsFragmentButtons extends Fragment {
    private Button logOut, createRoute, changePass;
    private FirebaseAuth auth;
    private FragmentManager fragmentManager;
    private ChangePasswordFragment changePasswordFragment;
    private OwnRouteFragment ownRouteFragment;

    public SettingsFragmentButtons() {
        // Required empty public constructor
    }

    public static SettingsFragmentButtons newInstance(String param1, String param2) {
        SettingsFragmentButtons fragment = new SettingsFragmentButtons();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_settings_buttons, container, false);

        auth = FirebaseAuth.getInstance();

        logOut = v.findViewById(R.id.logout);
        createRoute = v.findViewById(R.id.create_own_route);
        changePass = v.findViewById(R.id.change_password);

        fragmentManager = getFragmentManager();

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Log out current user
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent goToLoginScreen = new Intent(getContext(), LoginScreen.class);
                startActivity(goToLoginScreen);
                getActivity().finish();
                makeToast(R.string.toast_logged_out);
            }
        });

        //Switch fragments to create own route fragment
        createRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCreateRouteFragment();
                fragmentManager.beginTransaction().replace(R.id.settings_fragment_container, ownRouteFragment).commit();
            }
        });

        //Switch fragments to change password fragment
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPasswordFragment();
                fragmentManager.beginTransaction().replace(R.id.settings_fragment_container, changePasswordFragment).commit();
            }
        });
    }

    /**
     * Checks if there is a change password fragment. If there isn't, then make a new ChangePasswordFragment and commit
     */
    private void setPasswordFragment() {
        if(fragmentManager.findFragmentById(R.id.change_password_fragment) == null){
            changePasswordFragment = new ChangePasswordFragment();
            fragmentManager.beginTransaction().add(R.id.settings_fragment_container, changePasswordFragment).commit();
        } else {
            changePasswordFragment = (ChangePasswordFragment) fragmentManager.findFragmentById(R.id.change_password_fragment);
        }
        Data.getInstance().setChangePasswordFragment(changePasswordFragment);
    }

    /**
     * Checks if there is a create route fragment. If there isn't, then make a new OwnRouteFragment and commit
     */
    private void setCreateRouteFragment() {
        if(fragmentManager.findFragmentById(R.id.ownRouteFragment) == null){
            ownRouteFragment = new OwnRouteFragment();
            fragmentManager.beginTransaction().add(R.id.settings_fragment_container,ownRouteFragment).commit();
        } else {
            ownRouteFragment = (OwnRouteFragment) fragmentManager.findFragmentById(R.id.ownRouteFragment);
        }
        Data.getInstance().setOwnRouteFragment(ownRouteFragment);
    }

    /**
     * Make a new toast
     * @param message id of the string resource such that the text changes depending on the device language
     */
    private void makeToast(int message){
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}