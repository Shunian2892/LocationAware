package com.example.location_aware;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

public class OpenStreetMaps {
    private Polyline route;

    public void drawRoute(MapView mapView, ArrayList<GeoPoint> points){
        route = new Polyline();
        route.setPoints(points);
        Data.getInstance().setRouteLine(route);
        mapView.getOverlayManager().add(route);
    }

    public void clearRoute(){

    }
}
