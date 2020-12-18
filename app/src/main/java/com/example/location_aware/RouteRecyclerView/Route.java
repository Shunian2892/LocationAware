package com.example.location_aware.RouteRecyclerView;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class Route {
    private String name;
    private String[] places;
    private ArrayList<GeoPoint> geoPoints;
    private boolean isOwnMade;

    public Route(String name, String[] places){
        this.name = name;
        this.places = places;
        this.isOwnMade = false;
    }

    public Route(String name, ArrayList<GeoPoint> geoPoints){
        this.geoPoints =geoPoints;
        this.name = name;
        this.isOwnMade = true;

    }

    public ArrayList<GeoPoint> getGeoPoints() {
        return geoPoints;
    }

    public void setGeoPoints(ArrayList<GeoPoint> geoPoints) {
        this.geoPoints = geoPoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getPlaces() {
        return places;
    }

    public void setPlaces(String[] places) {
        this.places = places;
    }

    public String getStringPlaces(){
        String stringPlace = "";
        for (String place: places) {
            stringPlace +=" ";
            stringPlace += place;
        }
        return stringPlace;
    }

    public boolean isOwnMade() {
        return isOwnMade;
    }
}
