package com.example.location_aware;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.graphics.drawable.Drawable;
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

    private ArrayList<MethodItem> methods;
    private MethodAdapter methodAdapter;
    private Spinner methodChoices;
    private ImageButton startRoute, stopRoute;
    private boolean routeVisible;

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

        routeVisible = false;

        //Initialise floating action button and onClickListener
        setCenter = findViewById(R.id.centerMap);
        setCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentLocation = Data.getInstance().getCurrentLocation();
                mapController = Data.getInstance().getMapController();
                mapController.setCenter(currentLocation);
                System.out.println(currentLocation.toString());
            }
        });

        mapView = Data.getInstance().getMapView();
        routeService = new OpenRouteService(mapView);
        Data.getInstance().setRoute(routeService);

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
                Toast.makeText(MainActivity.this, clickedItemName + " selected", Toast.LENGTH_SHORT).show();

                // TODO: 15-12-2020 Change this so that it sends the correct method to the api for calling a new route!!
                switch (clickedItemName){
                    case "Walking":
                        //Send "foot-walking" to API call
                        Data.getInstance().setRouteMethod("foot-walking");
                        break;
                    case "Cycling":
                        //Send "cycling-regular" to API call
                        Data.getInstance().setRouteMethod("cycling-regular");
                        break;
                    case "Driving":
                        //Send "driving-car" to API call
                        Data.getInstance().setRouteMethod("driving-car");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Do nothing
            }
        });

        startRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              routeService.getRoute(new GeoPoint(51.5897, 4.7616),
                        new GeoPoint(51.5957, 4.7795),
                        Data.getInstance().getRouteMethod());
              startRoute.setEnabled(false);
              startRoute.setImageResource(R.drawable.start_route_disabled);
              stopRoute.setEnabled(true);
              stopRoute.setImageResource(R.drawable.stop_route);
            }
        });

        stopRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapView.getOverlays().remove(routeService);
                stopRoute.setEnabled(false);
                stopRoute.setImageResource(R.drawable.stop_route_disabled);
                startRoute.setEnabled(true);
                startRoute.setImageResource(R.drawable.start_route);
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
}