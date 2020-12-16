package com.example.location_aware;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.location_aware.RouteRecyclerView.Route;
import com.example.location_aware.RouteRecyclerView.SetRoute;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.IOException;
import java.util.ArrayList;

public class MapFragment extends Fragment implements SetRoute {
    private Context context;

    private MapView map;
    private IMapController controller;
    private LocationManager manager;
    private LocationListener listener;

    private Marker currentLocationMarker;
    private GpsMyLocationProvider myLocationProvider;
    private MyLocationNewOverlay myLocationNewOverlay;


    private ArrayList<GeoPoint> points;
    private OpenStreetMaps osm;
    private GeoPoint startPoint, newMarker, endPoint, currentLocation,startMarker;
    private boolean firstLocationSet;
    private ImageButton startRoute, stopRoute;
    private FloatingActionButton setCenter;
    private EditText startLocationInput, endLocationInput;
    private Spinner methodChoices;
    private ArrayList<MethodItem> methods;

    private OpenStreetMaps streetMaps;
    private IMapController mapController;
    private MethodAdapter methodAdapter;


    private OpenRouteService routeService;
    private Boolean clicked;

    public MapFragment() {
        // Required empty public constructor
    }


    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Load/initialise osmdroid configuration
        context = getActivity().getApplicationContext();
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));

        View v = inflater.inflate(R.layout.fragment_map, container, false);

        createMap(v);
        routeService = new OpenRouteService(map);
        Data.getInstance().setRouteService(routeService);
        streetMaps = Data.getInstance().getStreetMaps();


        clicked = false;
        //Initialize buttons
        startRoute = v.findViewById(R.id.start_route_button);
        stopRoute = v.findViewById(R.id.stop_route_button);
        stopRoute.setEnabled(false);
        setCenter = v.findViewById(R.id.centerMap);

        //Initialize EditText box
        startLocationInput = v.findViewById(R.id.start_location);
        endLocationInput = v.findViewById(R.id.end_location);

        //Initialise arraylist with different methods
        initSpinnerList();
        methodChoices = v.findViewById(R.id.spinner);

        return v;
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

    /**
     * Delete route from map
     */
    private void clearRoute(){
        streetMaps.clearRoute();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Get location
        getLocation();
        addLocations();
        //Set OnClickListeners
        //Set clicked boolean on true and draw route
        startRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked = true;
                String startLocation = startLocationInput.getText().toString();
                String endLocation = endLocationInput.getText().toString();

                try {
                    startPoint = streetMaps.createGeoPoint(getActivity().getApplicationContext(), startLocation);
                    endPoint = streetMaps.createGeoPoint(getActivity().getApplicationContext(), endLocation);
                    drawRoute(startPoint, endPoint);
                    startRoute.setEnabled(false);
                    startRoute.setImageResource(R.drawable.start_route_disabled);
                    stopRoute.setEnabled(true);
                    stopRoute.setImageResource(R.drawable.stop_route);
                    Toast.makeText(getContext(), "Starting route! A moment please...", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Please type in a start/end point!", Toast.LENGTH_LONG).show();
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
                Toast.makeText(getContext(), "Stopping route! A moment please...", Toast.LENGTH_LONG).show();
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



        //Set adapter to the spinner and a setOnItemSelectedListener.
        methodAdapter = new MethodAdapter(getContext(), methods);
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
        });
    }

    /**
     * Get the user location and show it on the map.
     */
    public void getLocation() {
        manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        listener = location -> {
            if(!firstLocationSet){
                //Get current location and set a new GeoPoint with the current latitude and longitude. Set point in center of screen
                startMarker = new GeoPoint(location.getLatitude(), location.getLongitude());
                controller.setCenter(startMarker);
                firstLocationSet = true;
                newMarker = startMarker;
            } else {
                //Update new current location without centering the map
                newMarker = new GeoPoint(location.getLatitude(), location.getLongitude());
            }

            Data.getInstance().setCurrentLocation(newMarker);

            //Make new marker for the new location, delete old marker, and display new marker on map
            Marker newPosition = new Marker(map);
            newPosition.setPosition(newMarker);
            map.getOverlays().remove(currentLocationMarker);
            currentLocationMarker = newPosition;
            map.getOverlays().add(newPosition);
        };

        //If GPS permission is granted, keep checking for location changes. If so, update marker
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        }
    }

    public void addLocations(){
        points.add(new GeoPoint(51.5897, 4.7616));
        points.add(new GeoPoint(51.5890, 4.7758));
        points.add(new GeoPoint(51.5957, 4.7795));
        points.add(new GeoPoint(51.5859, 4.7924));

        Data.getInstance().setGeoPointsList(points);
    }
    public void createMap(View view){
        map = (MapView) view.findViewById(R.id.osm_view);
        map.setUseDataConnection(true);
        map.setTileSource(TileSourceFactory.MAPNIK);
        Data.getInstance().setMapView(map);

        //Set mapcontroller
        controller = map.getController();
        controller.setZoom(18);
        Data.getInstance().setMapController(controller);

        //Zoom in with pinching
        map.setMultiTouchControls(true);
        map.setBuiltInZoomControls(true);

        //Set GPS location provider
        myLocationProvider = new GpsMyLocationProvider(getActivity());

        //Set location overlay to show location on map
        myLocationNewOverlay = new MyLocationNewOverlay(myLocationProvider, map);
        myLocationNewOverlay.enableMyLocation();
        myLocationNewOverlay.disableFollowLocation();
        Data.getInstance().setMyLocationNewOverlay(myLocationNewOverlay);

        points = new ArrayList<>();
        osm = new OpenStreetMaps();
        firstLocationSet = false;

        routeService = new OpenRouteService(map);

        //Check for GPS permission on first use
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
        }
    }

    @Override
    public void setRouteCoord(Route route) {
        GeoPoint[] geoPoints = new GeoPoint[route.getPlaces().length];
        try {
            for (int i =0; i<geoPoints.length;i++) {
                geoPoints[i] = streetMaps.createGeoPoint(getContext(),route.getPlaces()[i]);
                Log.d("MainRouteCoord", geoPoints[i].toIntString());
            }
            clearRoute();
            drawRoute(geoPoints);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Please chose a valid route", Toast.LENGTH_LONG).show();
        }
    }
}


