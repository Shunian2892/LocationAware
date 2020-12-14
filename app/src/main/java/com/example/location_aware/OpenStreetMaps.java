package com.example.location_aware;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

public class OpenStreetMaps {

    public void drawRoute(MapView mapView, ArrayList<GeoPoint> points){
        Polyline route = new Polyline();
        route.setPoints(points);
        mapView.getOverlayManager().add(route);
    }

}
