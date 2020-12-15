package com.example.location_aware;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

public class OpenStreetMaps {
    private Polyline route;
    private MapView mapView;

    public OpenStreetMaps(){
        this.mapView = Data.getInstance().getMapView();
    }

    public void drawRoute(ArrayList<GeoPoint> points){
        route = new Polyline();
        route.setPoints(points);
        Data.getInstance().setRouteLine(route);
        mapView.getOverlayManager().add(route);
    }

    public void clearRoute(){
        mapView.getOverlayManager().remove(route);
    }
}
