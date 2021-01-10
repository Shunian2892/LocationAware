package com.example.location_aware;

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

import com.google.firebase.auth.FirebaseAuth;

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
        setCreateRouteFragment();
        setPasswordFragment();

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

        createRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction().show(ownRouteFragment).commit();
                fragmentManager.beginTransaction().hide(changePasswordFragment).commit();
            }
        });

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction().show(changePasswordFragment).commit();
                fragmentManager.beginTransaction().hide(ownRouteFragment).commit();
            }
        });
    }


    private void setPasswordFragment() {
        if(fragmentManager.findFragmentById(R.id.change_password_fragment) == null){
            changePasswordFragment = new ChangePasswordFragment();
            fragmentManager.beginTransaction().add(R.id.settings_fragment_container, changePasswordFragment).commit();
        } else {
            changePasswordFragment = (ChangePasswordFragment) fragmentManager.findFragmentById(R.id.change_password_fragment);
        }
        Data.getInstance().setChangePasswordFragment(changePasswordFragment);
        fragmentManager.beginTransaction().hide(changePasswordFragment).commit();
    }

    private void setCreateRouteFragment() {
        if(fragmentManager.findFragmentById(R.id.ownRouteFragment) == null){
            ownRouteFragment = new OwnRouteFragment();
            fragmentManager.beginTransaction().add(R.id.settings_fragment_container,ownRouteFragment).commit();
        } else {
            ownRouteFragment = (OwnRouteFragment) fragmentManager.findFragmentById(R.id.ownRouteFragment);
        }

        Data.getInstance().setOwnRouteFragment(ownRouteFragment);
        fragmentManager.beginTransaction().hide(ownRouteFragment).commit();

    }



    private void makeToast(int message){
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}