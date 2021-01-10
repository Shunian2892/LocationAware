package com.example.location_aware;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {
    private TextView settingsTv;
    private FragmentManager fragmentManager;
    private SettingsFragmentButtons settingsButtons;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        settingsTv = v.findViewById(R.id.settings_tv);

        fragmentManager = getFragmentManager();
        setSettingsButtonsFragment();

        return v;
    }

    private void setSettingsButtonsFragment(){
        if(fragmentManager.findFragmentById(R.id.settings_buttons_fragment) == null){
            settingsButtons = new SettingsFragmentButtons();
            fragmentManager.beginTransaction().add(R.id.settings_fragment_container, settingsButtons).commit();
        } else {
            settingsButtons = (SettingsFragmentButtons) fragmentManager.findFragmentById(R.id.settings_buttons_fragment);
        }
        Data.getInstance().setSettingsButtons(settingsButtons);

        fragmentManager.beginTransaction().show(settingsButtons).commit();
    }

}