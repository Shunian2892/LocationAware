package com.example.location_aware.logic.routeRecyclerView;

import org.osmdroid.util.GeoPoint;
import java.util.ArrayList;


/**
 * This class makes a new Route object with a name and an array list of geopoints or a object with a start and end geopoint and a method.
 */
public class Route {
    private String name, method;
    private ArrayList<GeoPoint> geoPoints;
    private GeoPoint start, end;

    public Route(String name, ArrayList<GeoPoint> geoPoints) {
        this.geoPoints = geoPoints;
        this.name = name;
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

    public String getMethod() {
        return method;
    }

    public GeoPoint getStart() {
        return start;
    }

    public GeoPoint getEnd() {
        return end;
    }
}
