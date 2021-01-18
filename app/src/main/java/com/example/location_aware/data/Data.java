package com.example.location_aware.data;

import android.widget.Spinner;

import com.example.location_aware.logic.IMarkerUpdateListener;
import com.example.location_aware.ui.MapFragment;
import com.example.location_aware.logic.OpenStreetMaps;
import com.example.location_aware.ui.SettingsFragmentButtons;
import com.example.location_aware.logic.routeRecyclerView.*;
import com.example.location_aware.logic.spinner.*;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Singleton class which holds most of the information
 */
public class Data {
    private static Data data;
    private MapView mapView;
    private IMapController mapController;
    private GeoPoint currentLocation;
    private String routeMethod;
    private Polyline routeLine;
    private OpenStreetMaps streetMaps;
    private Route route, spinnerRoute;
    private RouteAdapter routeAdapter;
    private ArrayList<Route> routeList;
    private HashMap<String, ArrayList<String>> routeHashMap;
    private String currentUser;
    private ArrayList<DogWalkingItem> dogWalkingItems;
    private MapFragment mapFragment;
    private SettingsFragmentButtons settingsButtons;
    private IMarkerUpdateListener markerUpdateListener;
    private boolean clicked;

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
}
