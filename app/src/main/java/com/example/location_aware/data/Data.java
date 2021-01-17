package com.example.location_aware.data;

import android.widget.Spinner;

import com.example.location_aware.ui.ChangePasswordFragment;
import com.example.location_aware.logic.IMarkerUpdateListener;
import com.example.location_aware.ui.MapFragment;
import com.example.location_aware.logic.OpenRouteService;
import com.example.location_aware.logic.OpenStreetMaps;
import com.example.location_aware.ui.OwnRouteFragment;
import com.example.location_aware.ui.SettingsFragment;
import com.example.location_aware.ui.SettingsFragmentButtons;
import com.example.location_aware.logic.routeRecyclerView.*;
import com.example.location_aware.logic.spinner.*;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Singleton class which holds most of the information
 */
public class Data {
    private static Data data;
    private MyLocationNewOverlay myLocationNewOverlay;
    private MapView mapView;
    private IMapController mapController;
    private GeoPoint currentLocation;
    private String routeMethod;
    private Polyline routeLine;
    private OpenStreetMaps streetMaps;
    private OpenRouteService routeService;
    private Route route, spinnerRoute;
    private RouteAdapter routeAdapter;
    private ArrayList<Route> routeList;
    private HashMap<String, ArrayList<String>> routeHashMap;
    private String currentUser;
    private ArrayList<DogWalkingItem> dogWalkingItems;
    private MapFragment mapFragment;
    private OwnRouteFragment ownRouteFragment;
    private RouteRV routeRV;
    private ChangePasswordFragment changePasswordFragment;
    private SettingsFragment settingsFragment;
    private SettingsFragmentButtons settingsButtons;
    private IMarkerUpdateListener markerUpdateListener;
    private boolean clicked;
    private Spinner methodChoices, dogParkChoices;

    public static Data getInstance(){
        if(data == null){
            data = new Data();
            return data;
        } else {
            return data;
        }
    }

    public static Data getData() {
        return data;
    }

    public static void setData(Data data) {
        Data.data = data;
    }

    public MapFragment getMapFragment() {
        return mapFragment;
    }

    public void setMapFragment(MapFragment mapFragment) {
        this.mapFragment = mapFragment;
    }

    public OwnRouteFragment getOwnRouteFragment() {
        return ownRouteFragment;
    }

    public void setOwnRouteFragment(OwnRouteFragment ownRouteFragment) {
        this.ownRouteFragment = ownRouteFragment;
    }

    public RouteRV getRouteRV() {
        return routeRV;
    }

    public void setRouteRV(RouteRV routeRV) {
        this.routeRV = routeRV;
    }

    public MyLocationNewOverlay getMyLocationNewOverlay() {
        return myLocationNewOverlay;
    }

    public void setMyLocationNewOverlay(MyLocationNewOverlay myLocationNewOverlay) {
        this.myLocationNewOverlay = myLocationNewOverlay;
    }

    public MapView getMapView() {
        return mapView;
    }

    public void setMapView(MapView mapView) {
        this.mapView = mapView;
    }

    public IMapController getMapController() {
        return mapController;
    }

    public void setMapController(IMapController mapController) {
        this.mapController = mapController;
    }

    public GeoPoint getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(GeoPoint currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getRouteMethod() {
        return routeMethod;
    }

    public void setRouteMethod(String routeMethod) {
        this.routeMethod = routeMethod;
    }

    public void setRouteLine(Polyline routeLine) {
        this.routeLine = routeLine;
    }

    public Polyline getRouteLine() {
        return routeLine;
    }

    public OpenStreetMaps getStreetMaps() {
        return streetMaps;
    }

    public void setStreetMaps(OpenStreetMaps streetMaps) {
        this.streetMaps = streetMaps;
    }

    public OpenRouteService getRouteService() {
        return routeService;
    }

    public void setRouteService(OpenRouteService routeService) {
        this.routeService = routeService;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public RouteAdapter getRouteAdapter() {
        return routeAdapter;
    }

    public void setRouteAdapter(RouteAdapter routeAdapter) {
        this.routeAdapter = routeAdapter;
    }

    public HashMap<String, ArrayList<String>> getRouteHashMap() {
        return routeHashMap;
    }

    public void setRouteHashMap(HashMap<String, ArrayList<String>> routeHashMap) {
        this.routeHashMap = routeHashMap;
    }

    public void addToRouteHashMap(String route, ArrayList<String> locations){
        this.routeHashMap.put(route,locations);
    }

    public ArrayList<Route> getRouteList() {
        return routeList;
    }

    public void setRouteList(ArrayList<Route> routeList) {
        this.routeList = routeList;
    }

    public void addRoute(Route route){
        this.routeList.add(route);
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public ArrayList<DogWalkingItem> getDogWalkingItems() {
        return dogWalkingItems;
    }

    public void setDogWalkingItems(ArrayList<DogWalkingItem> dogWalkingItems) {
        this.dogWalkingItems = dogWalkingItems;
    }

    public ChangePasswordFragment getChangePasswordFragment() {
        return changePasswordFragment;
    }

    public void setChangePasswordFragment(ChangePasswordFragment changePasswordFragment) {
        this.changePasswordFragment = changePasswordFragment;
    }

    public SettingsFragment getSettingsFragment() {
        return settingsFragment;
    }

    public void setSettingsFragment(SettingsFragment settingsFragment) {
        this.settingsFragment = settingsFragment;
    }

    public SettingsFragmentButtons getSettingsButtons() {
        return settingsButtons;
    }

    public void setSettingsButtons(SettingsFragmentButtons settingsButtons) {
        this.settingsButtons = settingsButtons;
    }

    public IMarkerUpdateListener getMarkerUpdateListener() {
        return markerUpdateListener;
    }

    public void setMarkerUpdateListener(IMarkerUpdateListener markerUpdateListener) {
        this.markerUpdateListener = markerUpdateListener;
    }

    public Route getSpinnerRoute() {
        return spinnerRoute;
    }

    public void setSpinnerRoute(Route spinnerRoute) {
        this.spinnerRoute = spinnerRoute;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public Spinner getMethodChoices() {
        return methodChoices;
    }

    public void setMethodChoices(Spinner methodChoices) {
        this.methodChoices = methodChoices;
    }

    public Spinner getDogParkChoices() {
        return dogParkChoices;
    }

    public void setDogParkChoices(Spinner dogParkChoices) {
        this.dogParkChoices = dogParkChoices;
    }
}
