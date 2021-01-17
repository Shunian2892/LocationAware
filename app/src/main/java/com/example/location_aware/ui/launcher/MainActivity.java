package com.example.location_aware.ui.launcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.location_aware.data.Data;
import com.example.location_aware.ui.LoginScreen;
import com.example.location_aware.ui.MapFragment;
import com.example.location_aware.ui.OwnRouteFragment;
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
    private String TAG = "MAINACTIVITY CLASS";

    private FragmentManager fragmentManager;
    private MapFragment mapFragment;
    private RouteRV routeRV;

    private SettingsFragment settingsFragment;

    private ArrayList<Route> routeList;
    private HashMap<String,ArrayList<String>> nameList;
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        //Change fragments based on which button the user clicks
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_map:
                            setMapFragment();
                            fragmentManager.beginTransaction().replace(R.id.fragment_container,mapFragment).commit();
                            break;
                        case R.id.menu_list:
                            setRouteFragment();
                            fragmentManager.beginTransaction().replace(R.id.fragment_container,routeRV).commit();
                            break;
                        case R.id.menu_makeRoute:
                            setSettingsFragment();
                            fragmentManager.beginTransaction().replace(R.id.fragment_container, settingsFragment).commit();
                            break;
                    }
                    return true;
                }
            };

    private FirebaseAuth auth;
    private FirebaseUser user;

    private String[] currentUser;
    private String userPathSubstring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Load shared preferences data
        loadData();

        //Set the different fragments
        fragmentManager = getSupportFragmentManager();
        setMapFragment();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        Data.getInstance().setRouteHashMap(new HashMap<>());

        //Initialize authentication listener for Firebase Database
        auth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapFragment.clearMap();
        user = auth.getCurrentUser();

        if(user != null){
            currentUser = user.getEmail().split(Pattern.quote("@"));
            userPathSubstring = currentUser[0];
            Data.getInstance().setCurrentUser(userPathSubstring);
            makeToast(R.string.toast_logged_in);
        } else {
            //TODO finish this if/else statement. doenst have to do anything tbh...
        }
    }

    /**
     * Checks if there is a map fragment. If there isn't, then make a new MapFragment and commit
     */
    public void setMapFragment(){
        if(fragmentManager.findFragmentById(R.id.map_fragment) == null){
            mapFragment= new MapFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_container, mapFragment).commit();
        } else {
            mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map_fragment);
        }

        Data.getInstance().setMapFragment(mapFragment);
        fragmentManager.beginTransaction().replace(R.id.fragment_container,mapFragment).commit();
    }

    /**
     * Checks if there is a route recyclerview fragment. If there isn't, then make a new RouteRV and commit
     */
    public void setRouteFragment(){
        if(fragmentManager.findFragmentById(R.id.route_rv_fragment) == null){
            routeRV = new RouteRV();

            fragmentManager.beginTransaction().add(R.id.fragment_container,routeRV).commit();
        } else {
            routeRV = (RouteRV) fragmentManager.findFragmentById(R.id.route_rv_fragment);
        }
//        routeRV.setRoute(mapFragment.getSetRoute());
        Data.getInstance().setRouteRV(routeRV);
    }

    /**
     * Checks if there is a settings fragment. If there isn't, then make a new SettingsFragment and commit
     */
    public void setSettingsFragment(){
        if(fragmentManager.findFragmentById(R.id.settings_fragment) == null){
            settingsFragment = new SettingsFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_container,settingsFragment).commit();
        } else {
            settingsFragment = (SettingsFragment) fragmentManager.findFragmentById(R.id.settings_fragment);
        }

        Data.getInstance().setSettingsFragment(settingsFragment);
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
     * log out method, sign out of the firebase user and return to login screen
     * @param view
     */
    public void logout(View view) {
        auth.signOut();
        Intent goToLoginScreen = new Intent(getApplicationContext(), LoginScreen.class);
        startActivity(goToLoginScreen);
        finish();
        makeToast(R.string.toast_logged_out);
    }

    /**
     * Make a new toast
     * @param message id of the string resource such that the text changes depending on the device language
     */
    private void makeToast(int message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}