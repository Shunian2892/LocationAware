package com.example.location_aware.logic.geofencing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.location_aware.R;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import static android.content.ContentValues.TAG;

/**
 * This class handles the different transition types when entering, exiting or dwelling in a set geofence radius. For each transition type, the user will get a toast message.
 */
public class GeoFenceBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError()) {
            Log.d(TAG, "onReceive BrCastReceiver Error ");
            return;
        }

        int transitionType = geofencingEvent.getGeofenceTransition();

        //Check which transition type is given, and show a toast accordingly
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                makeToast(context, R.string.toast_geofence_enter);
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                makeToast(context, R.string.toast_geofence_dwell);
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                makeToast(context, R.string.toast_geofence_exit);
                break;
        }
    }

    /**
     * Make a new toast
     * @param context context of the application
     * @param message id of the string resource such that the text changes depending on the device language
     */
    private void makeToast(Context context, int message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
