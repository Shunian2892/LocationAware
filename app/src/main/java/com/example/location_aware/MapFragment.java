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

import com.example.location_aware.firebase.User;
import com.example.location_aware.geofencing.GeofenceSetup;
import com.example.location_aware.RouteRecyclerView.Route;
import com.example.location_aware.RouteRecyclerView.SetRoute;
import com.example.location_aware.spinner.DogWalkingAdapter;
import com.example.location_aware.spinner.DogWalkingItem;
import com.example.location_aware.spinner.MethodAdapter;
import com.example.location_aware.spinner.MethodItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class MapFragment extends Fragment implements SetRoute{
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
    private EditText startLocationInput;
    private Spinner methodChoices, dogParkChoices;
    private ArrayList<MethodItem> methods;
    private ArrayList<DogWalkingItem> dogParks;

    private OpenStreetMaps streetMaps;
    private IMapController mapController;
    private MethodAdapter methodAdapter;
    private DogWalkingAdapter dogWalkingAdapter;

    private GeofenceSetup setupexe;

    private OpenRouteService routeService;
    private Boolean clicked;
    private DatabaseHelper databaseHelper;


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

        databaseHelper = new DatabaseHelper();

        //Create mapView and draw marker on current location
        createMap(v);
        streetMaps = Data.getInstance().getStreetMaps();


        clicked = false;
        //Initialize buttons
        startRoute = v.findViewById(R.id.start_route_button);
        stopRoute = v.findViewById(R.id.stop_route_button);
        stopRoute.setEnabled(false);
        setCenter = v.findViewById(R.id.centerMap);

        //Initialize EditText box
        startLocationInput = v.findViewById(R.id.start_location);

        //Initialise arraylist with different methods
        initSpinnerList();
        methodChoices = v.findViewById(R.id.spinner);
        dogParkChoices = v.findViewById(R.id.end_location);

        setupexe = new GeofenceSetup(getActivity().getApplicationContext(), getActivity());
        setupexe.setUpGeofencing();

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Get location
        getLocation();
        //addLocations();

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
                            streetMaps.clearRoute();
                            drawRoute(startPoint, endPoint, Data.getInstance().getRouteMethod());
                        }
                        break;
                    case "Cycling":
                        Data.getInstance().setRouteMethod("cycling-regular");
                        if(clicked){
                            streetMaps.clearRoute();
                            drawRoute(startPoint, endPoint, Data.getInstance().getRouteMethod());
                        }
                        break;
                    case "Driving":
                        Data.getInstance().setRouteMethod("driving-car");
                        if(clicked){
                            streetMaps.clearRoute();
                            drawRoute(startPoint, endPoint, Data.getInstance().getRouteMethod());
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Do nothing
            }
        });

        dogWalkingAdapter = new DogWalkingAdapter(getContext(), dogParks);
        dogParkChoices.setAdapter(dogWalkingAdapter);
        dogParkChoices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                DogWalkingItem clickedItem = (DogWalkingItem) adapterView.getItemAtPosition(position);
                endPoint = clickedItem.getLocation();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Do nothing
            }
        });

        //Set OnClickListeners
        //Set clicked boolean on true and draw route
        startRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked = true;
                String startLocation = startLocationInput.getText().toString();

                startPoint = streetMaps.createGeoPoint(getContext(), startLocation);

                if(startPoint == null){
                    Toast.makeText(getContext(), "Please type in a (valid) start point!", Toast.LENGTH_LONG).show();
                } else {
                    Log.d("ONCLICK MapFragment", startPoint + " " + endPoint);
                    Log.d("DataONCLICK mapfragment", Data.getInstance().getStreetMaps().toString());
                    streetMaps.clearRoute();
                    drawRoute(startPoint, endPoint, Data.getInstance().getRouteMethod());
                    startRoute.setEnabled(false);
                    startRoute.setImageResource(R.drawable.start_route_disabled);
                    stopRoute.setEnabled(true);
                    stopRoute.setImageResource(R.drawable.stop_route);
                    Toast.makeText(getContext(), "Starting route! A moment please...", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Set clicked boolean to false, such that when switching methods doesn't draw more routes
        stopRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked = false;
                streetMaps.clearRoute();
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
    }

    /**
     * Initialise different methods in the methods list
     */
    private void initSpinnerList(){
        methods = new ArrayList<>();
        methods.add(new MethodItem("Walking", R.drawable.walking));
        methods.add(new MethodItem("Cycling", R.drawable.bike));
        methods.add(new MethodItem("Driving", R.drawable.car));

        dogParks = new ArrayList<>();
        dogParks.add(new DogWalkingItem("Mastbos", R.drawable.forrest, new GeoPoint(51.56134525467572, 4.767793972942238)));
        dogParks.add(new DogWalkingItem("Liesbos", R.drawable.forrest, new GeoPoint(51.58769, 4.7105)));
        dogParks.add(new DogWalkingItem("Somerweide", R.drawable.dog_park, new GeoPoint(51.61157, 4.74524)));
        dogParks.add(new DogWalkingItem("Melkpad", R.drawable.dog_park, new GeoPoint(51.609804, 4.729187)));
        dogParks.add(new DogWalkingItem("Johansberg", R.drawable.dog_park, new GeoPoint(51.6152369049874, 4.72776872907537)));
        dogParks.add(new DogWalkingItem("Cadetten kamp", R.drawable.forrest, new GeoPoint(51.6039713573722, 4.83417502727798)));
        dogParks.add(new DogWalkingItem("Klein Ardennen", R.drawable.dog_park, new GeoPoint(51.61157, 4.7808913298782100)));
        dogParks.add(new DogWalkingItem("Loopakker", R.drawable.dog_park, new GeoPoint(51.60618999687, 4.737446)));

        Data.getInstance().setDogWalkingItems(dogParks);

    }

    /**
     * Get the user location and show it on the map.
     */
    public void getLocation() {
        manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        listener = location -> {
            if(getView()== null){
                return;
            }
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

            // TODO: 1-1-2021 maybe see if we can fix the double data saving
            //Set current location in Data Singleton
            Data.getInstance().setCurrentLocation(newMarker);

            //Get information of specific user from Firebase Database
            Data.getInstance().setCurrentUser(databaseHelper.getCurrentUserDatabase());

            setupexe.setOwnGeofence(Data.getInstance().getCurrentLocation());
            //Set current location in Firebase Database in the subbranch of the current user
            databaseHelper.updateUserValues();

            databaseHelper.getDbData(this);
            //Make new marker for the new location, delete old marker, and display new marker on map
            Marker newPosition = new Marker(map);
            newPosition.setPosition(newMarker);
            map.getOverlays().remove(currentLocationMarker);
            currentLocationMarker = newPosition;
            currentLocationMarker.setTitle("You are here");
            map.getOverlays().add(newPosition);
        };

        //If GPS permission is granted, keep checking for location changes. If so, update marker
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        }
    }



    /**
     * Draw other users on map
     * @param user
     */
    public void drawOtherUser(User user) {
        double lat = user.getLatitude();
        double lon = user.getLongitude();
        GeoPoint otherUserLocation = new GeoPoint(lat, lon);

        Marker otherUserMarker = new Marker(Data.getInstance().getMapView());
        otherUserMarker.setPosition(otherUserLocation);
        Data.getInstance().getMapView().getOverlays().add(otherUserMarker);
    }

    /**
     * Create the map for the mapView fragment. Sets the controller, location provider, marker, and openRouteService for drawing routes between points.
     * @param view
     */
    public void createMap(View view){
        map = (MapView) view.findViewById(R.id.osm_view);
        map.setUseDataConnection(true);
        map.setTileSource(TileSourceFactory.MAPNIK);
        Data.getInstance().setMapView(map);

        routeService = new OpenRouteService(map);
        Data.getInstance().setRouteService(routeService);

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

    /**
     * Draws a route on the map between two points
     * @param start startpoint
     * @param end endpoint
     * @param method method used: walking, cycling, or driving
     */
    public void drawRoute(GeoPoint start, GeoPoint end, String method){
        routeService.getRoute(start, end, method);
    }

    /**
     * Gets a route from the recyclerviewer that contains multiple locations and draws this on the map
     * @param route
     */
    @Override
    public void setRouteCoord(Route route) {
        //Log.d("MainActivity Rote", route.getStringPlaces());
        ArrayList<GeoPoint> geoPoints = new ArrayList<>();

        if(!route.isOwnMade()){
            if(geoPoints == null){
                Toast.makeText(context, "Please chose a valid route", Toast.LENGTH_LONG).show();
            } else {
                for (int i =0; i<route.getPlaces().length;i++) {
                    geoPoints.add(streetMaps.createGeoPoint(context, route.getPlaces()[i]));
                }
            }
        } else {
            geoPoints = route.getGeoPoints();
        }
        streetMaps.clearRoute();
        routeService.getRoute(geoPoints, Data.getInstance().getRouteMethod(), "en");
    }

    public SetRoute getSetRoute(){
        return this;
    }

    public void clearMap(){
        map.getOverlays().clear();
    }

}


