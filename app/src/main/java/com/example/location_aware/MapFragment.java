package com.example.location_aware;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {
    MapView map;
    IMapController controller;
    LocationService manager;

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
        Context context = getActivity().getApplicationContext();
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

        GeoPoint startLocation =
                new GeoPoint(51.5719, 4.7683);

        controller.setCenter(startLocation);

        manager = new LocationService();

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
}