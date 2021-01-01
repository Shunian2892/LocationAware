package com.example.location_aware;

import com.example.location_aware.RouteRecyclerView.Route;
import com.example.location_aware.RouteRecyclerView.RouteAdapter;
import com.example.location_aware.RouteRecyclerView.RouteManager;
import com.example.location_aware.firebase.Database;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.HashMap;

public class Data {
    private static Data data;
    private MyLocationNewOverlay myLocationNewOverlay;
    private MapView mapView;
    private IMapController mapController;
    private GeoPoint currentLocation;
    private ArrayList<GeoPoint> geoPointsList;
    private String routeMethod;
    private Polyline routeLine;
    private OpenStreetMaps streetMaps;
    private OpenRouteService routeService;
    private Route route;
    private RouteManager routeManager;
    private RouteAdapter routeAdapter;
    private ArrayList<Route> routeList;
    private ArrayList<String> nameList;
    private HashMap<String, ArrayList<String>> routeHashMap;
    private Database myDb;

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

    public ArrayList<GeoPoint> getGeoPointsList() {
        return geoPointsList;
    }

    public void setGeoPointsList(ArrayList<GeoPoint> geoPointsList) {
        this.geoPointsList = geoPointsList;
    }

    public String getRouteMethod() {
        return routeMethod;
    }

    public void setRouteMethod(String routeMethod) {
        this.routeMethod = routeMethod;
    }

    public Polyline getRouteLine() {
        return routeLine;
    }

    public void setRouteLine(Polyline routeLine) {
        this.routeLine = routeLine;
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

    public RouteManager getRouteManager() {
        return routeManager;
    }

    public void setRouteManager(RouteManager routeManager) {
        this.routeManager = routeManager;
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

    public ArrayList<String> getNameList() {
        return nameList;
    }

    public void setNameList(ArrayList<String> nameList) {
        this.nameList = nameList;
    }

    public Database getMyDb() {
        return myDb;
    }

    public void setMyDb(Database myDb) {
        this.myDb = myDb;
    }
}
