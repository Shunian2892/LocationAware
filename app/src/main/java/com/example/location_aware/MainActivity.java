package com.example.location_aware;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.location_aware.RouteRecyclerView.Route;
import com.example.location_aware.RouteRecyclerView.RouteRV;
import com.example.location_aware.RouteRecyclerView.SetRoute;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private MapFragment mapFragment;
    private RouteRV routeRV;
    private OwnRouteFragment ownRouteFragment;
    private OpenStreetMaps streetMaps;
    private OpenRouteService routeService;
    private ArrayList<Route> routeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadData();
        fragmentManager = getSupportFragmentManager();
        setMapFragment();
        setRouteFragment();
        setOwnRouteFragment();

        streetMaps = Data.getInstance().getStreetMaps();
        routeService = Data.getInstance().getRouteService();
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);


    }
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
    public void setMapFragment(){
        if(fragmentManager.findFragmentById(R.id.map_fragment) == null){
            mapFragment= new MapFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_container, mapFragment).commit();
        }else{
            mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map_fragment);
        }
        fragmentManager.beginTransaction().show(mapFragment).commit();
    }

    public void setRouteFragment(){
        if(fragmentManager.findFragmentById(R.id.route_rv_fragment) == null){
            routeRV = new RouteRV();

            routeRV.setRoute(mapFragment.getSetRoute());
            fragmentManager.beginTransaction().add(R.id.fragment_container,routeRV).commit();
        }else{
            routeRV = (RouteRV) fragmentManager.findFragmentById(R.id.route_rv_fragment);
        }
        //routeRV.saveData();
        //routeRV.loadData();
        fragmentManager.beginTransaction().hide(routeRV).commit();
    }

    public void setOwnRouteFragment(){
        if(fragmentManager.findFragmentById(R.id.ownRouteFragment) == null){
            ownRouteFragment = new OwnRouteFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_container,ownRouteFragment).commit();
        }else{
            ownRouteFragment = (OwnRouteFragment) fragmentManager.findFragmentById(R.id.ownRouteFragment);
        }
        fragmentManager.beginTransaction().hide(ownRouteFragment).commit();
    }



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
    }

}