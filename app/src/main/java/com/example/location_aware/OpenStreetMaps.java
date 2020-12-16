package com.example.location_aware;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    public GeoPoint createGeoPoint(Context context, String myLocation) throws IOException {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocationName(myLocation, 1);
        Address address = addresses.get(0);
        double longitude = address.getLongitude();
        double latitude = address.getLatitude();
        GeoPoint newPoint = new GeoPoint(latitude, longitude);
        return newPoint;
    }
    public void drawMarker(MapView mapView, GeoPoint point, Drawable icon){
        Marker marker = new Marker(mapView);
        marker.setPosition(point);
        marker.setIcon(icon);
        mapView.getOverlays().add(marker);
    }
}
