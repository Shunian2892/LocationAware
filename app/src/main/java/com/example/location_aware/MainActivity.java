package com.example.location_aware;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private MapFragment mapFragment;
    private FloatingActionButton setCenter;
    private MapView mapView;
    private IMapController mapController;
    private GeoPoint currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        if(fragmentManager.findFragmentById(R.id.map_fragment) == null){
            mapFragment = new MapFragment();
            fragmentManager.beginTransaction().add(R.id.map_fragment, mapFragment).commit();
        } else {
            mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map_fragment);
        }

        setCenter = findViewById(R.id.centerMap);
        setCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentLocation = Data.getInstance().getCurrentLocation();
                mapController = Data.getInstance().getMapController();
                mapController.setCenter(currentLocation);
                System.out.println(currentLocation.toString());
            }
        });
    }
}