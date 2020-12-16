package com.example.location_aware;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.location_aware.RouteRecyclerView.Route;
import com.example.location_aware.RouteRecyclerView.RouteRV;
import com.example.location_aware.RouteRecyclerView.SetRoute;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private MapFragment mapFragment;
    private MapView mapView;
    private IMapController mapController;
    private GeoPoint currentLocation, startPoint, endPoint;
    private OpenRouteService routeService;
    private OpenStreetMaps streetMaps;

    private ImageButton startRoute, stopRoute;
    private FloatingActionButton setCenter;
    private EditText startLocationInput, endLocationInput;
    private Spinner methodChoices;
    private ArrayList<MethodItem> methods;
    private MethodAdapter methodAdapter;
    private RouteRV routeRV;


    private boolean clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();

        if(fragmentManager.findFragmentById(R.id.map_fragment) == null){
            mapFragment = new MapFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_container, mapFragment).commit();
        } else {
            mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map_fragment);
        }
        setContentView(R.layout.activity_main);

        /*clicked = false;

        mapView = Data.getInstance().getMapView();
        routeService = new OpenRouteService(mapView);
        Data.getInstance().setRouteService(routeService);
        streetMaps = Data.getInstance().getStreetMaps();

        //Initialize buttons
        startRoute = findViewById(R.id.start_route_button);
        stopRoute = findViewById(R.id.stop_route_button);
        stopRoute.setEnabled(false);
        setCenter = findViewById(R.id.centerMap);

        //Initialize EditText box
        startLocationInput = findViewById(R.id.start_location);
        endLocationInput = findViewById(R.id.end_location);


        //Set OnClickListeners
        //Set clicked boolean on true and draw route
        startRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked = true;
                String startLocation = startLocationInput.getText().toString();
                String endLocation = endLocationInput.getText().toString();

                try {
                    startPoint = streetMaps.createGeoPoint(MainActivity.this, startLocation);
                    endPoint = streetMaps.createGeoPoint(MainActivity.this, endLocation);
                    drawRoute(startPoint, endPoint);
                    startRoute.setEnabled(false);
                    startRoute.setImageResource(R.drawable.start_route_disabled);
                    stopRoute.setEnabled(true);
                    stopRoute.setImageResource(R.drawable.stop_route);
                    Toast.makeText(MainActivity.this, "Starting route! A moment please...", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Please type in a start/end point!", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Set clicked boolean to false, such that when switching methods doesn't draw more routes
        stopRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked = false;
                clearRoute();
                stopRoute.setEnabled(false);
                stopRoute.setImageResource(R.drawable.stop_route_disabled);
                startRoute.setEnabled(true);
                startRoute.setImageResource(R.drawable.start_route);
                Toast.makeText(MainActivity.this, "Stopping route! A moment please...", Toast.LENGTH_LONG).show();
            }
        });

        setCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentLocation = Data.getInstance().getCurrentLocation();
                mapController = Data.getInstance().getMapController();
                mapController.animateTo(currentLocation);

            }
        });

        //Initialise arraylist with different methods
        initSpinnerList();
        methodChoices = findViewById(R.id.spinner);

        //Set adapter to the spinner and a setOnItemSelectedListener.
        methodAdapter = new MethodAdapter(this, methods);
        methodChoices.setAdapter(methodAdapter);
        methodChoices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                MethodItem clickedMethod = (MethodItem) adapterView.getItemAtPosition(position);
                String clickedItemName = clickedMethod.getMethodName();

                switch (clickedItemName){
                    case "Walking":
                        Data.getInstance().setRouteMethod("foot-walking");
                        if(clicked){
                            drawRoute(startPoint, endPoint);
                        }
                        break;
                    case "Cycling":
                        Data.getInstance().setRouteMethod("cycling-regular");
                        if(clicked){
                            drawRoute(startPoint, endPoint);
                        }
                        break;
                    case "Driving":
                        Data.getInstance().setRouteMethod("driving-car");
                        if(clicked){
                            drawRoute(startPoint, endPoint);
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Do nothing
            }
        });*/
    }

    /**
     * Initialise different methods in the methods list
     */
    /*private void initSpinnerList(){
        methods = new ArrayList<>();
        methods.add(new MethodItem("Walking", R.drawable.walking));
        methods.add(new MethodItem("Cycling", R.drawable.bike));
        methods.add(new MethodItem("Driving", R.drawable.car));
    }

    *//**
     * Clear any already drawn routes and draw a new route on map
     *//*
    private void drawRoute(GeoPoint startPoint, GeoPoint endPoint){
        clearRoute();
        routeService.getRoute(startPoint,
            endPoint,
            Data.getInstance().getRouteMethod());
    }

    private void drawRoute(GeoPoint[] geoPoints){
        clearRoute();
        routeService.getRoute(geoPoints,Data.getInstance().getRouteMethod(),"en");
    }

    *//**
     * Delete route from map
     *//*
    private void clearRoute(){
        streetMaps.clearRoute();
    }

    @Override
    public void setRouteCoord(Route route) {
        GeoPoint[] geoPoints = new GeoPoint[route.getPlaces().length];
        try {
        for (int i =0; i<geoPoints.length;i++) {
            geoPoints[i] = streetMaps.createGeoPoint(MainActivity.this,route.getPlaces()[i]);
            Log.d("MainRouteCoord", geoPoints[i].toIntString());
        }
        clearRoute();
        drawRoute(geoPoints);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Please chose a valid route", Toast.LENGTH_LONG).show();
        }

    }*/

}