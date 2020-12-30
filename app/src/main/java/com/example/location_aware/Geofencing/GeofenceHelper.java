package com.example.location_aware.Geofencing;

import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;

import org.osmdroid.util.GeoPoint;

class GeofenceHelper extends ContextWrapper {
    private PendingIntent pendingIntent;

    public GeofenceHelper(Context base) {
        super(base);
    }

    public GeofencingRequest getGeofencingRequest(Geofence geofence){
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofence(geofence);
        return builder.build();
    }

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

    public PendingIntent getPendingIntent(){
        if(pendingIntent != null) return pendingIntent;

        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,2607,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return  pendingIntent;
    }
}
