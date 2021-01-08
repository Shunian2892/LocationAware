package com.example.location_aware;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.example.location_aware.RouteRecyclerView.Route;

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
    private OpenRouteService routeService;

    public OpenStreetMaps(){
        this.mapView = Data.getInstance().getMapView();
        this.routeService = Data.getInstance().getRouteService();
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

    public GeoPoint createGeoPoint(Context context, String myLocation) {
        GeoPoint newPoint = null;
        Geocoder geocoder;
        List<Address> addresses;

        try{
            geocoder = new Geocoder(context, Locale.getDefault());

            addresses = geocoder.getFromLocationName(myLocation, 1);

            if(addresses.size() == 1){
                Address address = addresses.get(0);
                double longitude = address.getLongitude();
                double latitude = address.getLatitude();
                newPoint = new GeoPoint(latitude, longitude);
            }
        } catch (IOException e){
            return newPoint;
        }
        return newPoint;
    }

    public void drawMarker(MapView mapView, GeoPoint point, Drawable icon){
        Marker marker = new Marker(mapView);
        marker.setPosition(point);
        marker.setIcon(icon);
        mapView.getOverlays().add(marker);
    }

    public void drawMarker(MapView mapView, GeoPoint point, String userName, IMarkerUpdateListener markerUpdateListener){
        if(mapView != null){
            Marker marker = new Marker(mapView);
            marker.setPosition(point);
            marker.setTitle(userName);
            mapView.getOverlays().add(marker);
        }

    }

}
