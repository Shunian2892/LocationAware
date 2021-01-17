package com.example.location_aware.logic.routeRecyclerView;

import com.example.location_aware.data.Data;
import com.google.android.gms.location.Geofence;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class makes a new Route object with a name and either an array list of geopoints or a string array with location names.
 */
public class Route {
    private String name, method;
    private String[] places;
    private ArrayList<GeoPoint> geoPoints;
    private GeoPoint start, end;
    private boolean isOwnMade;

    public Route(String name, String[] places) {
        this.name = name;
        this.places = places;
        this.isOwnMade = false;
    }

    public Route(String name, ArrayList<GeoPoint> geoPoints) {
        this.geoPoints = geoPoints;
        this.name = name;
        this.isOwnMade = true;
    }

    public Route(GeoPoint start, GeoPoint end, String method){
        this.start = start;
        this.end = end;
        this.method = method;
    }

    public ArrayList<GeoPoint> getGeoPoints() {
        return geoPoints;
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

    public boolean isOwnMade() {
        return isOwnMade;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public GeoPoint getStart() {
        return start;
    }

    public GeoPoint getEnd() {
        return end;
    }
}
