package com.example.location_aware;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private MapFragment mapFragment;
    private FloatingActionButton setCenter;
    private MapView mapView;
    private IMapController mapController;
    private GeoPoint currentLocation;
    private OpenRouteService routeService;
    private OpenStreetMaps streetMaps;

    private ArrayList<MethodItem> methods;
    private MethodAdapter methodAdapter;
    private Spinner methodChoices;
    private ImageButton startRoute, stopRoute;
    private boolean clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        if(fragmentManager.findFragmentById(R.id.map_fragment) == null){
            mapFragment = new MapFragment();
            fragmentManager.beginTransaction().add(R.id.map_fragment, mapFragment).commit();
        } else {
            mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map_fragment);
        }

        clicked = false;

        //Initialise floating action button and onClickListener
        setCenter = findViewById(R.id.centerMap);
        setCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentLocation = Data.getInstance().getCurrentLocation();
                mapController = Data.getInstance().getMapController();
                mapController.animateTo(currentLocation);
            }
        });

        mapView = Data.getInstance().getMapView();
        routeService = new OpenRouteService(mapView);
        Data.getInstance().setRouteService(routeService);
        streetMaps = Data.getInstance().getStreetMaps();

        startRoute = findViewById(R.id.start_route_button);
        stopRoute = findViewById(R.id.stop_route_button);
        stopRoute.setEnabled(false);

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
                            drawRoute();
                        }
                        break;
                    case "Cycling":
                        Data.getInstance().setRouteMethod("cycling-regular");
                        if(clicked){
                            drawRoute();
                        }
                        break;
                    case "Driving":
                        Data.getInstance().setRouteMethod("driving-car");
                        if(clicked){
                            drawRoute();
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Do nothing
            }
        });

        //Set clicked boolean on true and draw route
        startRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked = true;
                drawRoute();
                startRoute.setEnabled(false);
                startRoute.setImageResource(R.drawable.start_route_disabled);
                stopRoute.setEnabled(true);
                stopRoute.setImageResource(R.drawable.stop_route);
                Toast.makeText(MainActivity.this, "Starting route! A moment please...", Toast.LENGTH_LONG).show();
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
    }

    /**
     * Initialise different methods in the methods list
     */
    private void initSpinnerList(){
        methods = new ArrayList<>();
        methods.add(new MethodItem("Walking", R.drawable.walking));
        methods.add(new MethodItem("Cycling", R.drawable.bike));
        methods.add(new MethodItem("Driving", R.drawable.car));
    }

    /**
     * Clear any already drawn routes and draw a new route on map
     */
    private void drawRoute(){
        clearRoute();
        routeService.getRoute(new GeoPoint(51.5897, 4.7616),
            new GeoPoint(51.5957, 4.7795),
            Data.getInstance().getRouteMethod());
    }

    /**
     * Delete route from map
     */
    private void clearRoute(){
        streetMaps.clearRoute();
    }

}