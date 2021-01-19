package com.example.location_aware.ui.launcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.location_aware.data.Data;
import com.example.location_aware.ui.LoginScreen;
import com.example.location_aware.ui.MapFragment;
import com.example.location_aware.R;
import com.example.location_aware.ui.SettingsFragment;
import com.example.location_aware.logic.routeRecyclerView.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * This class handles everything on the main activity. After logging in, the user will be redirected to this screen. Here the user can switch between three fragments: map, routes, and create own route.
 * When this activity is called, it will get the current user from the firebase database based on the email that was used to sign in, and the current username is set in the Data singleton.
 */
public class MainActivity extends AppCompatActivity {
    private String TAG = "MAINACTIVITY";

    private FragmentManager fragmentManager;
    private MapFragment mapFragment;
    private RouteRV routeRV;
    private SettingsFragment settingsFragment;
    private Fragment currentFragment;

    private FirebaseAuth auth;
    private FirebaseUser user;

    private String[] currentUser;
    private String userPathSubstring;

    private ArrayList<Route> routeList;
    private HashMap<String,ArrayList<String>> nameList;
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                /**
                 * Change fragments based on which button the user clicks
                 * @param item the item on teh bottomnav that is clicked
                 * @return
                 */
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    currentFragment = null;
                    switch (item.getItemId()) {
                        case R.id.menu_map:
                            setMapFragment();
                            fragmentManager.beginTransaction().replace(R.id.fragment_container,mapFragment).commit();
                            currentFragment = mapFragment;
                            break;
                        case R.id.menu_list:
                            setRouteFragment();
                            fragmentManager.beginTransaction().replace(R.id.fragment_container,routeRV).commit();
                            currentFragment = routeRV;
                            break;
                        case R.id.menu_makeRoute:
                            setSettingsFragment();
                            fragmentManager.beginTransaction().replace(R.id.fragment_container, settingsFragment).commit();
                            currentFragment = settingsFragment;
                            break;
                    }
                    return true;
                }
            };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Load shared preferences data
        loadData();

        //Set the different fragments
        fragmentManager = getSupportFragmentManager();
        if(savedInstanceState == null){
            setMapFragment();
            fragmentManager.beginTransaction().replace(R.id.fragment_container,mapFragment).commit();
        }



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        Data.getInstance().setRouteHashMap(new HashMap<>());

        Data.getInstance().setClicked(false);
        //Initialize authentication listener for Firebase Database
        auth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //if the mapfragment is not null clear the map when mapfragment starts
        if(mapFragment != null) {
            mapFragment.clearMap();
        }
        user = auth.getCurrentUser();

        if(user != null){
            currentUser = user.getEmail().split(Pattern.quote("@"));
            userPathSubstring = currentUser[0];
            Data.getInstance().setCurrentUser(userPathSubstring);
            makeToast(R.string.toast_logged_in);
        }
    }

    /**
     * Checks if there is a map fragment. If there isn't, then make a new MapFragment
     */
    public void setMapFragment(){
        if(fragmentManager.findFragmentById(R.id.map_fragment) == null){
            mapFragment= new MapFragment();
        } else {
            mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map_fragment);
        }
        Data.getInstance().setMapFragment(mapFragment);
    }

    /**
     * Checks if there is a route recyclerview fragment. If there isn't, then make a new RouteRV
     */
    public void setRouteFragment(){
        if(fragmentManager.findFragmentById(R.id.route_rv_fragment) == null){
            routeRV = new RouteRV();
        } else {
            routeRV = (RouteRV) fragmentManager.findFragmentById(R.id.route_rv_fragment);
        }
    }

    /**
     * Checks if there is a settings fragment. If there isn't, then make a new SettingsFragment
     */
    public void setSettingsFragment(){
        if(fragmentManager.findFragmentById(R.id.settings_fragment) == null){
            settingsFragment = new SettingsFragment();
        } else {
            settingsFragment = (SettingsFragment) fragmentManager.findFragmentById(R.id.settings_fragment);
        }
    }

    /**
     * Load the saved data from Shared Preferences
     */
    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonList = sharedPreferences.getString("route list",null);
        Type type = new TypeToken<ArrayList<Route>>(){}.getType();
        routeList = gson.fromJson(jsonList,type);

        if(routeList == null){
            routeList = new ArrayList<>();
        }

        Data.getInstance().setRouteList(routeList);

        SharedPreferences hmSharedPref = getSharedPreferences("hashmap", Context.MODE_PRIVATE);
        String defValue = new Gson().toJson(new HashMap<String, ArrayList<String>>());
        String jsonNames = hmSharedPref.getString("location name list", defValue);
        TypeToken<HashMap<String, ArrayList<String>>> nameToken = new TypeToken<HashMap<String, ArrayList<String>>>(){};
        nameList = new Gson().fromJson(jsonNames, nameToken.getType());

        Data.getInstance().setRouteHashMap(nameList);
    }

    /**
     * Make a new toast
     * @param message id of the string resource such that the text changes depending on the device language
     */
    private void makeToast(int message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}