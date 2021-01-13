package com.example.location_aware.logic.routeRecyclerView;

import com.example.location_aware.data.Data;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class makes a new Route object with a name and either an array list of geopoints or a string array with location names.
 */
public class Route {
    private String name;
    private String[] places;
    private ArrayList<GeoPoint> geoPoints;
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
}
