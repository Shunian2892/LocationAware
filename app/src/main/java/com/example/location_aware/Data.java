package com.example.location_aware;

import com.example.location_aware.RouteRecyclerView.Route;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

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
}
