package com.example.location_aware;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.location_aware.RouteRecyclerView.Route;
import com.example.location_aware.RouteRecyclerView.RouteRV;
import com.example.location_aware.RouteRecyclerView.SetRoute;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private MapFragment mapFragment;
    private RouteRV routeRV;
    private OwnRouteFragment ownRouteFragment;
    private OpenStreetMaps streetMaps;
    private OpenRouteService routeService;
    private ArrayList<Route> routeList;
    private HashMap<String,ArrayList<String>> nameList;
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_map:
                            //setMapFragment(fm);
                            fragmentManager.beginTransaction().show(mapFragment).commit();
                            fragmentManager.beginTransaction().hide(routeRV).commit();
                            fragmentManager.beginTransaction().hide(ownRouteFragment).commit();
                            break;
                        case R.id.menu_list:
                            fragmentManager.beginTransaction().show(routeRV).commit();
                            fragmentManager.beginTransaction().hide(mapFragment).commit();
                            fragmentManager.beginTransaction().hide(ownRouteFragment).commit();
                            break;
                        case R.id.menu_makeRoute:
                            fragmentManager.beginTransaction().show(ownRouteFragment).commit();
                            fragmentManager.beginTransaction().hide(routeRV).commit();
                            fragmentManager.beginTransaction().hide(mapFragment).commit();
                            break;
                    }
                    return true;
                }
            };
    private Button logout;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();

        //Set the different fragments
        fragmentManager = getSupportFragmentManager();
        setMapFragment();
        setRouteFragment();
        setOwnRouteFragment();

        //Initialize OSM and OpenRouteService
        streetMaps = Data.getInstance().getStreetMaps();
        routeService = Data.getInstance().getRouteService();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        Data.getInstance().setRouteHashMap(new HashMap<>());

        //Initialize authentication listener for Firebase Database
        authListener = Data.getData().getAuthStateListener();
        auth = FirebaseAuth.getInstance();

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(), "Logged out!", Toast.LENGTH_LONG).show();
                Intent goToLoginScreen = new Intent(getApplicationContext(), LoginScreen.class);
                startActivity(goToLoginScreen);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authListener);
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
        fragmentManager.beginTransaction().hide(routeRV).commit();
    }

    /**
     * Checks if there is a set own route fragment. If there isn't, then make a new OwnRouteFragment and commit
     */
    public void setOwnRouteFragment(){
        if(fragmentManager.findFragmentById(R.id.ownRouteFragment) == null){
            ownRouteFragment = new OwnRouteFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_container,ownRouteFragment).commit();
        } else {
            ownRouteFragment = (OwnRouteFragment) fragmentManager.findFragmentById(R.id.ownRouteFragment);
        }
        fragmentManager.beginTransaction().hide(ownRouteFragment).commit();
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
}