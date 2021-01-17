package com.example.location_aware.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.location_aware.R;
import com.example.location_aware.data.Data;

/**
 * This class is the settings fragment. This fragment contains a container in which three fragments will be cycled: the buttons fragment, the create own route fragment, and the change password fragment.
 * It will start off on the buttons fragment, from there the user can choose what to do next.
 */
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
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        settingsTv = v.findViewById(R.id.settings_tv);

        fragmentManager = getFragmentManager();
        setSettingsButtonsFragment();
        fragmentManager.beginTransaction().replace(R.id.settings_fragment_container, settingsButtons).commit();
        return v;
    }

    /*@Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null){
            //Data.getInstance().getSettingsFragment();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, Data.getInstance().getSettingsFragment());
        }
    }*/

    /**
     * Checks if there is a settings buttons fragment. If there isn't, then make a new SettingsButtonsFragment and commit
     */
    private void setSettingsButtonsFragment(){
        if(fragmentManager.findFragmentById(R.id.settings_buttons_fragment) == null){
            settingsButtons = new SettingsFragmentButtons();
        } else {
            settingsButtons = (SettingsFragmentButtons) fragmentManager.findFragmentById(R.id.settings_buttons_fragment);
        }
        Data.getInstance().setSettingsButtons(settingsButtons);

    }
}