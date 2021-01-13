package com.example.location_aware.logic.geofencing;

import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;

import org.osmdroid.util.GeoPoint;

/**
 * This class handles all pending intents, geofencing requests, and sets new geofences
 */
class GeofenceHelper extends ContextWrapper {
    private PendingIntent pendingIntent;

    public GeofenceHelper(Context base) {
        super(base);
    }

    /**
     * Create new Geofence request
     * @param geofence the location on which a geofence has to be set
     * @return return the request
     */
    public GeofencingRequest getGeofencingRequest(Geofence geofence){
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofence(geofence);
        return builder.build();
    }

    /**
     * Set a new geofence based on a given geopoint location
     * @param id unique id of the new geofence
     * @param point geopoint with latitude and longitude of the location
     * @param radius radius of the geofence around the location
     * @param transitionTypes transition type of user, can be: entering, dewelling, or exiting
     * @return return the geofence
     */
    public Geofence getGeofence(String id, GeoPoint point, float radius, int transitionTypes){
        Geofence geofence = new Geofence.Builder()
                .setCircularRegion(point.getLatitude(),point.getLongitude(),radius)
                .setRequestId(id)
                .setTransitionTypes(transitionTypes)
                .setLoiteringDelay(5000)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
        return geofence;
    }

    /**
     * Makes a new intent for transition types
     * @return pending intent
     */
    public PendingIntent getPendingIntent(){
        if(pendingIntent != null) return pendingIntent;

        Intent intent = new Intent(this, GeoFenceBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,2607,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }
}
