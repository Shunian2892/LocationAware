package com.example.location_aware.logic.geofencing;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.location_aware.data.Data;
import com.example.location_aware.logic.spinner.*;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.osmdroid.util.GeoPoint;

/**
 * This class sets new geofences on given locations
 */
public class GeofenceSetup {
    private Context context;
    private Activity activity;
    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;
    private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;

    public GeofenceSetup(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }

    public void setUpGeofencing(){
        checkFineLocationPermission();

        Log.d("GeofenceSetup", "setUp geofencing.......");

        geofencingClient = LocationServices.getGeofencingClient(context);
        geofenceHelper = new GeofenceHelper(context);

        if (Build.VERSION.SDK_INT >= 29) {
            //If API is higher then 29 we need background permission
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                addFences();
            } else {
                //Permission is not granted!! Need to request it..
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                    //We show a dialog and ask for permission
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                }
            }
        } else {
            addFences();
        }
    }

    /**
     * Add new locations on which a geofence needs to be created with a standard radius
     */
    private void addFences() {
        for (DogWalkingItem location : Data.getInstance().getDogWalkingItems()){
            addGeoFence(location.getLocation(),100,location.getName());
        }
    }

    /**
     * Add a new geofence on the map
     * @param geoPoint given location on which the geofence has te be set
     * @param radius radius of the geofence
     * @param id unique geofence id
     */
    private void addGeoFence(GeoPoint geoPoint, float radius, String id){
        checkFineLocationPermission();

        Geofence geofence = geofenceHelper.getGeofence(id,geoPoint,radius, Geofence.GEOFENCE_TRANSITION_ENTER|Geofence.GEOFENCE_TRANSITION_DWELL|Geofence.GEOFENCE_TRANSITION_EXIT);

        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);
        PendingIntent pendingIntent = geofenceHelper.getPendingIntent();

        geofencingClient.addGeofences(geofencingRequest,pendingIntent).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("GeofencingSetup", "/////////////////////////Geofence "+ geofence.getRequestId() + " is added");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("GeofencingSetup", e.getLocalizedMessage());
            }
        });
    }

    /**
     * Check if the user allowed location permission
     */
    private void checkFineLocationPermission() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
    }
}