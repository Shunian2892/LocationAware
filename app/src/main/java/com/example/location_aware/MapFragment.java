package com.example.location_aware;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {
    MapView map;
    IMapController controller;
    LocationService manager;
    GeoPoint startLocation;
    FusedLocationProviderClient locationProviderClient;
    Context context;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Load/initialise osmdroid configuration
        context = getActivity().getApplicationContext();
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));

        View v = inflater.inflate(R.layout.fragment_map, container, false);

        map = (MapView) v.findViewById(R.id.osm_view);
        map.setUseDataConnection(true);
        map.setTileSource(TileSourceFactory.MAPNIK);

//        //Zoom in with pinching
//        map.setMultiTouchControls(true);
//        map.setBuiltInZoomControls(true);

        controller = map.getController();
        controller.setZoom(14);

        GpsMyLocationProvider myLocationProvider = new GpsMyLocationProvider(getActivity());
        MyLocationNewOverlay locationOverlay = new MyLocationNewOverlay(myLocationProvider, map);
        locationOverlay.enableMyLocation();
        locationOverlay.enableFollowLocation();

        locationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        getLocation();

//        if(myLocationProvider.getLastKnownLocation() == null){
//            System.out.println("location is null");
//            startLocation = new GeoPoint(51.5719, 4.7683);
//            controller.setCenter(startLocation);
//        } else {
//            System.out.println("location is: " + myLocationProvider.getLastKnownLocation());
//            startLocation = new GeoPoint(myLocationProvider.getLastKnownLocation());
//        }

//        locationOverlay.runOnFirstFix(new Runnable() {
//            @Override
//            public void run() {
//                map.getOverlays().clear();
//                map.getOverlays().add(locationOverlay);
//                controller.animateTo(locationOverlay.getMyLocation());
//            }
//        });

        return v;
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
            return;
        }

        locationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                //startLocation = new GeoPoint(51.5719, 4.7683);
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder coder = new Geocoder(context, Locale.getDefault());
                        List<Address> addressList = coder.getFromLocation(location.getLatitude(),
                                location.getLongitude(),
                                1);

                        Log.d("Lat", "Latitude: " + addressList.get(0).getLatitude());
                        Log.d("Lon", "Longitude: " + addressList.get(0).getLongitude());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}