package com.example.location_aware;

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

import com.example.location_aware.RouteRecyclerView.Route;
import com.example.location_aware.RouteRecyclerView.RouteRV;
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
    private OwnRouteFragment ownRouteFragment;

    private SettingsFragment settingsFragment;

    private ArrayList<Route> routeList;
    private HashMap<String,ArrayList<String>> nameList;
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_map:
                            fragmentManager.beginTransaction().show(mapFragment).commit();
                            fragmentManager.beginTransaction().hide(routeRV).commit();
                            fragmentManager.beginTransaction().hide(settingsFragment).commit();
                            break;
                        case R.id.menu_list:
                            fragmentManager.beginTransaction().show(routeRV).commit();
                            fragmentManager.beginTransaction().hide(mapFragment).commit();
                            fragmentManager.beginTransaction().hide(settingsFragment).commit();
                            break;
                        case R.id.menu_makeRoute:
                            fragmentManager.beginTransaction().show(settingsFragment).commit();
                            fragmentManager.beginTransaction().hide(routeRV).commit();
                            fragmentManager.beginTransaction().hide(mapFragment).commit();
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
        setRouteFragment();
        setSettingsFragment();

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
//            Log.d(TAG, "onAuthStateChanged: signed_in under name: " + userPathSubstring + " with ID: " + user.getUid());
            makeToast(R.string.toast_logged_in);
        } else {

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
        fragmentManager.beginTransaction().show(mapFragment).commit();
    }

    /**
     * Checks if there is a route recyclerview fragment. If there isn't, then make a new RouteRV and commit
     */
    public void setRouteFragment(){
        if(fragmentManager.findFragmentById(R.id.route_rv_fragment) == null){
            routeRV = new RouteRV();
            routeRV.setRoute(mapFragment.getSetRoute());
            fragmentManager.beginTransaction().add(R.id.fragment_container,routeRV).commit();
        } else {
            routeRV = (RouteRV) fragmentManager.findFragmentById(R.id.route_rv_fragment);
        }
        Data.getInstance().setRouteRV(routeRV);
        fragmentManager.beginTransaction().hide(routeRV).commit();
    }

//    /**
//     * Checks if there is a set own route fragment. If there isn't, then make a new OwnRouteFragment and commit
//     */
//    public void setOwnRouteFragment(){
//        if(fragmentManager.findFragmentById(R.id.ownRouteFragment) == null){
//            ownRouteFragment = new OwnRouteFragment();
//            fragmentManager.beginTransaction().add(R.id.fragment_container,ownRouteFragment).commit();
//        } else {
//            ownRouteFragment = (OwnRouteFragment) fragmentManager.findFragmentById(R.id.ownRouteFragment);
//        }
//
//        Data.getInstance().setOwnRouteFragment(ownRouteFragment);
//        fragmentManager.beginTransaction().hide(ownRouteFragment).commit();
//    }

    public void setSettingsFragment(){
        if(fragmentManager.findFragmentById(R.id.settings_fragment) == null){
            settingsFragment = new SettingsFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_container,settingsFragment).commit();
        } else {
            settingsFragment = (SettingsFragment) fragmentManager.findFragmentById(R.id.settings_fragment);
        }

        Data.getInstance().setSettingsFragment(settingsFragment);
        fragmentManager.beginTransaction().hide(settingsFragment).commit();
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

    private void makeToast(int message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}