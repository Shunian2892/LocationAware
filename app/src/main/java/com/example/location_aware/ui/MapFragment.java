package com.example.location_aware.ui;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.location_aware.logic.OpenRouteService;
import com.example.location_aware.logic.OpenStreetMaps;
import com.example.location_aware.R;
import com.example.location_aware.data.Data;
import com.example.location_aware.data.DatabaseHelper;
import com.example.location_aware.data.firebase.*;
import com.example.location_aware.logic.geofencing.GeofenceSetup;
import com.example.location_aware.logic.IMarkerUpdateListener;
import com.example.location_aware.logic.MapHelper;
import com.example.location_aware.logic.routeRecyclerView.*;
import com.example.location_aware.logic.spinner.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * This class handles the map and the points and routes that are going to be drawn on the map
 */
public class MapFragment extends Fragment implements IMarkerUpdateListener {
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
    private AutoCompleteTextView startLocationInput;
    private String[] myLocation;
    private Spinner methodChoices, dogParkChoices;
    private ArrayList<MethodItem> methods;
    private ArrayList<DogWalkingItem> dogParks;

    private OpenStreetMaps streetMaps;
    private IMapController mapController;
    private MethodAdapter methodAdapter;
    private DogWalkingAdapter dogWalkingAdapter;

    private GeofenceSetup geofenceSetup;

    private OpenRouteService routeService;
    private Boolean clicked;
    private DatabaseHelper databaseHelper;

    private MapHelper mapHelper;
    private HashMap<String, GeoPoint> userHashMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Load/initialise osmdroid configuration
        context = getActivity().getApplicationContext();
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        Data.getInstance().setMarkerUpdateListener(this);

        View v = inflater.inflate(R.layout.fragment_map, container, false);

        //Initialise database helper and other usher hashmap
        databaseHelper = new DatabaseHelper();
        userHashMap = new HashMap<>();

        //Create mapView and draw marker on current location
        createMap(v);
        streetMaps = Data.getInstance().getStreetMaps();

        //Initialize buttons
        startRoute = v.findViewById(R.id.start_route_button);
        startRoute.setEnabled(!Data.getInstance().isClicked());
        stopRoute = v.findViewById(R.id.stop_route_button);
        stopRoute.setEnabled(Data.getInstance().isClicked());

        if(Data.getInstance().isClicked()) {
            startRoute.setImageResource(R.drawable.start_route_disabled);
            stopRoute.setImageResource(R.drawable.stop_route);
        }
        setCenter = v.findViewById(R.id.centerMap);

        //Initialize EditText box
        startLocationInput = v.findViewById(R.id.start_location);
        myLocation = getResources().getStringArray(R.array.suggestions_array);
        startLocationInput.setAdapter(new ArrayAdapter<String>(context, R.layout.autocomplete_list_item, myLocation));

        //Initialise arraylist with different methods

        methodChoices =  v.findViewById(R.id.spinner);
        dogParkChoices = v.findViewById(R.id.end_location);
        initSpinnerList();

        //Initialize Geofence setup
        geofenceSetup = new GeofenceSetup(getActivity().getApplicationContext(), getActivity());
        geofenceSetup.setUpGeofencing();

        mapHelper = new MapHelper();



        //when the map is created and there is a route in the data instance, draw this route
        if(Data.getInstance().getRoute() != null){
            setRouteCoord(Data.getInstance().getRoute());
        }
        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        //when the mapfragment stops clear the route
        streetMaps.clearRoute();
    }

    /**
     * Save the chosen position from the method spinner with shared prefrences
     * @param position the position of the item on the spinner
     */
    private void saveMethodItem(int position){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("method",MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putInt("methodSpinner", position);
        prefEditor.apply();
        prefEditor.commit();
    }

    /**
     * Save the chosen position from the dog park/forest spinner with shared prefrences
     * @param position the position of the item on the spinner
     */
    private void saveParkItem(int position){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("park",MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putInt("parkSpinner",position);
        prefEditor.apply();
        prefEditor.commit();
    }

    /**
     * get the saved position of the item in the method spinner and set the methodspinner on that position
     */
    private void getSharedPrefMethod(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("method",MODE_PRIVATE);
        int spinnerMethodValue = sharedPref.getInt("methodSpinner",-1);
        if(spinnerMethodValue != -1){
            methodChoices.setSelection(spinnerMethodValue);
        }

    }
    /**
     * get the saved position of the item in the dog park/forest spinner and set the dog park/forest spinner on that position
     */
    private void getSharedPrefPark(){
        SharedPreferences sharedPrefPark = getActivity().getSharedPreferences("park",MODE_PRIVATE);
        int spinnerValuePark = sharedPrefPark.getInt("parkSpinner",-1);
        if(spinnerValuePark != -1) {
            dogParkChoices.setSelection(spinnerValuePark);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Get current location
        getLocation();

        //Set adapter to the spinner and a setOnItemSelectedListener.
        methodAdapter = new MethodAdapter(getContext(), methods);
        methodChoices.setAdapter(methodAdapter);
        getSharedPrefMethod();
        methodChoices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                MethodItem clickedMethod = (MethodItem) adapterView.getItemAtPosition(position);
                String clickedItemName = clickedMethod.getMethodName();

                //set the method based on the item name on the method spinner
                if(clickedItemName.equals(getResources().getString(R.string.walking))){
                    Data.getInstance().setRouteMethod("foot-walking");
                }else if(clickedItemName.equals(getResources().getString(R.string.cycling))) {
                    Data.getInstance().setRouteMethod("cycling-regular");
                }else if(clickedItemName.equals(getResources().getString(R.string.driving))) {
                    Data.getInstance().setRouteMethod("driving-car");
                }
                //save the chosen position
                saveMethodItem(position);
                //if the spinner route isn't null and a route is clicked, clear the map first and then draw the route
                if(Data.getInstance().getSpinnerRoute() != null){
                    if(Data.getInstance().isClicked()){
                        streetMaps.clearRoute();
                        drawRoute(Data.getInstance().getSpinnerRoute().getStart(), Data.getInstance().getSpinnerRoute().getEnd(), Data.getInstance().getRouteMethod());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Do nothing
            }
        });

        dogWalkingAdapter = new DogWalkingAdapter(getContext(), dogParks);
        dogParkChoices.setAdapter(dogWalkingAdapter);
        getSharedPrefPark();
        dogParkChoices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                DogWalkingItem clickedItem = (DogWalkingItem) adapterView.getItemAtPosition(position);
                endPoint = clickedItem.getLocation();
                saveParkItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Do nothing
            }
        });

        //Set OnClickListeners
        //Set clicked boolean on true and draw route
        startRoute.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                clicked = true;
                Data.getInstance().setClicked(true);
                String startLocation = startLocationInput.getText().toString();

                //Check if the input says one of the defined string in the myLocation array
                boolean contains = Arrays.stream(myLocation).anyMatch(startLocation::equals);

                if(contains){
                    startPoint = Data.getInstance().getCurrentLocation();
                } else {
                    startPoint = streetMaps.createGeoPoint(getContext(), startLocation);
                }

                if(startPoint == null){
                    makeToast(R.string.toast_valid_location);
                } else {
                    streetMaps.clearRoute();
                    Data.getInstance().setRoute(null);
                    drawRoute(startPoint, endPoint, Data.getInstance().getRouteMethod());
                    startRoute.setEnabled(!Data.getInstance().isClicked());
                    startRoute.setImageResource(R.drawable.start_route_disabled);
                    stopRoute.setEnabled(Data.getInstance().isClicked());
                    stopRoute.setImageResource(R.drawable.stop_route);
                    makeToast(R.string.toast_starting_route);
                }
            }
        });

        //Set clicked boolean to false, such that when switching methods doesn't draw more routes
        stopRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked = false;
                Data.getInstance().setClicked(false);
                streetMaps.clearRoute();
                Data.getInstance().setSpinnerRoute(null);
                stopRoute.setEnabled(Data.getInstance().isClicked());
                stopRoute.setImageResource(R.drawable.stop_route_disabled);
                startRoute.setEnabled(!Data.getInstance().isClicked());
                startRoute.setImageResource(R.drawable.start_route);
                makeToast(R.string.toast_stopping_route);
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
        methods.add(new MethodItem(getResources().getString(R.string.walking), R.drawable.walking));
        methods.add(new MethodItem(getResources().getString(R.string.cycling), R.drawable.bike));
        methods.add(new MethodItem(getResources().getString(R.string.driving), R.drawable.car));

        dogParks = new ArrayList<>();
        dogParks.add(new DogWalkingItem("Mastbos", R.drawable.forrest, new GeoPoint(51.56134525467572, 4.767793972942238)));
        dogParks.add(new DogWalkingItem("Liesbos", R.drawable.forrest, new GeoPoint(51.584596746744424, 4.694465396860606)));
        dogParks.add(new DogWalkingItem("Somerweide", R.drawable.dog_park, new GeoPoint(51.61157, 4.74524)));
        dogParks.add(new DogWalkingItem("Melkpad", R.drawable.dog_park, new GeoPoint(51.609804, 4.729187)));
        dogParks.add(new DogWalkingItem("Johansberg", R.drawable.dog_park, new GeoPoint(51.6152369049874, 4.72776872907537)));
        dogParks.add(new DogWalkingItem("Cadetten kamp", R.drawable.forrest, new GeoPoint(51.6039713573722, 4.83417502727798)));
        dogParks.add(new DogWalkingItem("Klein Ardennen", R.drawable.dog_park, new GeoPoint(51.606064, 4.781162)));
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

            //Set current location in Data Singleton
            Data.getInstance().setCurrentLocation(newMarker);

            databaseHelper.getCurrentUserDatabase();
            //Set current location in Firebase Database in the subbranch of the current user
            databaseHelper.updateUserValues();

            databaseHelper.getDbData();
            //Make new marker for the new location, delete old marker, and display new marker on map
            Marker newPosition = new Marker(map);
            newPosition.setPosition(newMarker);
            map.getOverlays().remove(currentLocationMarker);
            currentLocationMarker = newPosition;
            currentLocationMarker.setIcon(context.getDrawable(R.drawable.location_current_user));
            currentLocationMarker.setTitle(getResources().getString(R.string.you_are_here));
            map.getOverlays().add(newPosition);
        };

        //If GPS permission is granted, keep checking for location changes. If so, update marker
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        }
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

        routeService = new OpenRouteService();

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

        points = new ArrayList<>();
        osm = new OpenStreetMaps();
        firstLocationSet = false;

        routeService = new OpenRouteService();

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
        Route route = new Route(start, end, method);
        Data.getInstance().setSpinnerRoute(route);
        routeService.getRoute(Data.getInstance().getSpinnerRoute().getStart(), Data.getInstance().getSpinnerRoute().getEnd(),Data.getInstance().getSpinnerRoute().getMethod());
    }

    /**
     * Gets a route from the recyclerviewer that contains multiple locations and draws this on the map
     * @param route the route that is going to be drawn
     */
    public void setRouteCoord(Route route) {
        ArrayList<GeoPoint> geoPoints = route.getGeoPoints();
        streetMaps.clearRoute();
        routeService.getRoute(geoPoints, Data.getInstance().getRouteMethod(), "en");
    }

    /**
     * Clear the map
     */
    public void clearMap(){
        map.getOverlays().clear();
    }

    /**
     * Draw other user locations on map when their position is changed in the database
     * @param user
     */
    @Override
    public void onMarkerUpdate(User user) {

        double userLat  = user.getLatitude();
        double userLon = user.getLongitude();
        String userName = user.getName();

        GeoPoint userLocation = new GeoPoint(userLat, userLon);

        if(userHashMap.containsKey(userName)){
            if(!userHashMap.get(userName).equals(userLocation)){
                if((mapHelper.distanceCoords(Data.getInstance().getCurrentLocation().getLatitude(),Data.getInstance().getCurrentLocation().getLongitude(),userLat,userLon) < 300) && (map != null)) {
                    drawOtherUsers(userLocation, userName);
                }
            }
        }
        userHashMap.put(userName,userLocation);
    }

    public void drawOtherUsers(GeoPoint userLocation, String userName){
        Iterator hmIterator = userHashMap.entrySet().iterator();

        while (hmIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)hmIterator.next();
            GeoPoint newGeoPoint = (GeoPoint) mapElement.getValue();
            Marker marker = new Marker(map);
            marker.setPosition(newGeoPoint);
            streetMaps.drawMarker(map, marker, context.getDrawable(R.drawable.location_other_user));

            Log.d("draw other user markers", "drawOtherUsers: mapElement" + mapElement);

        }
    }

    /**
     * Make a new toast
     * @param messageID id of the string resource such that the text changes depending on the device language
     */
    private void makeToast(int messageID){
        Toast.makeText(context, messageID, Toast.LENGTH_LONG).show();
    }
}


