package com.example.location_aware.logic;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;

import com.example.location_aware.R;
import com.example.location_aware.data.Data;
import com.example.location_aware.logic.OpenRouteService;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This class draws routes, puts markers, and clears the mapview.
 */
public class OpenStreetMaps {
    private Polyline route;
    private MapView mapView;
    private OpenRouteService routeService;
    private Marker oldMarker;

    public OpenStreetMaps(){
        this.mapView = Data.getInstance().getMapView();
        this.routeService = Data.getInstance().getRouteService();
    }

    /**
     * Draw a new route based on the given GeoPoints
     * @param points the locations between which a route must be drawn
     */
    public void drawRoute(ArrayList<GeoPoint> points){
        route = new Polyline();
        route.setPoints(points);
        Data.getInstance().setRouteLine(route);
        mapView.getOverlayManager().add(route);
    }

    /**
     * Clear existing route from map
     */
    public void clearRoute(){
        mapView.getOverlayManager().remove(Data.getInstance().getRouteLine());
    }

    /**
     * Create a new Geopoint from a given string
     * @param context context of application
     * @param myLocation location of which a geopoint needs to be made
     * @return the newly created geopoint
     */
    public GeoPoint createGeoPoint(Context context, String myLocation) {
        GeoPoint newPoint = null;
        Geocoder geocoder;
        List<Address> addresses;

        try{
            geocoder = new Geocoder(context, Locale.getDefault());

            //Get address from given location
            addresses = geocoder.getFromLocationName(myLocation, 1);

            //If an address has been found, get it's longitude and latitude, then create a new geopoint
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

    /**
     * Draw a marker with a custom icon on the map
     * @param mapView map on which to draw
     * @param point location where the marker has to be set
     * @param icon icon to use as marker
     */
    public void drawMarker(MapView mapView, GeoPoint point, Drawable icon){
        Marker marker = new Marker(mapView);
        marker.setPosition(point);
        marker.setIcon(icon);
        mapView.getOverlays().add(marker);
    }

    /**
     * Draw a marker on the map and set the title with the given username
     * @param mapView map on which to draw
     * @param point location where the marker has to be set
     * @param userName user name to set as marker title
     */
    public void drawMarker(MapView mapView, GeoPoint point, String userName, Drawable otherUser){
        if(mapView != null){
            Marker marker = new Marker(mapView);
            marker.setPosition(point);
            marker.setIcon(otherUser);
            mapView.getOverlays().remove(oldMarker);
            oldMarker = marker;
            oldMarker.setIcon(otherUser);
            oldMarker.setTitle(userName);
            mapView.getOverlays().add(marker);
        }
    }
}
