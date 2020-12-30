package com.example.location_aware;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.location_aware.RouteRecyclerView.Route;
import com.example.location_aware.RouteRecyclerView.RouteAdapter;
import com.example.location_aware.RouteRecyclerView.RouteManager;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OwnRouteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OwnRouteFragment extends Fragment {
        private EditText routeName, newLocation;
        private Button addLocation, deleteLocation, createRoute;
        private ListView addedLocations;
        private ArrayList<GeoPoint> newPoints;
        private OpenStreetMaps streetMaps;
        private GeoPoint newGeoPoint;
        private ArrayAdapter<String> adapter;
        private ArrayList<String> locationNames;
        private RouteManager routeManager;
        private RouteAdapter routeAdapter;

    public OwnRouteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OwnRouteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OwnRouteFragment newInstance(String param1, String param2) {
        OwnRouteFragment fragment = new OwnRouteFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_own_route, container, false);

        streetMaps = Data.getInstance().getStreetMaps();
        routeManager = Data.getInstance().getRouteManager();
        routeAdapter = Data.getInstance().getRouteAdapter();

        newPoints = new ArrayList<>();
        locationNames = new ArrayList<>();

        routeName = v.findViewById(R.id.setName);
        newLocation = v.findViewById(R.id.setlocation);
        addLocation = v.findViewById(R.id.addButton);
        deleteLocation = v.findViewById(R.id.removeButton);
        createRoute = v.findViewById(R.id.createRouteButton);

        addedLocations = v.findViewById(R.id.addedLocations);
        adapter = new ArrayAdapter<String>(getContext(), R.layout.own_route_list_item, locationNames);
        addedLocations.setAdapter(adapter);

        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = newLocation.getText().toString();
                newGeoPoint = streetMaps.createGeoPoint(getContext(), location);

                if(newGeoPoint == null){
                    Toast.makeText(getContext(), "This location is not valid, try again", Toast.LENGTH_LONG).show();
                } else {
                    newPoints.add(newGeoPoint);
                    locationNames.add(location);
                    Log.d("Location added", "onClick: " + locationNames.toString());
                    adapter.notifyDataSetChanged();
                }
            }
        });

        deleteLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(locationNames.size() != 0 && newPoints.size() != 0){
                    locationNames.remove(locationNames.size()-1);
                    newPoints.remove(newPoints.size()-1);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        createRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = routeName.getText().toString();
                if(!name.equals("")){
                    if(newPoints.size() >= 2){
                        Data.getInstance().addRoute(new Route(name, newPoints, locationNames));
                        routeAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Route is created!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Please add multiple location!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Please type in a name!", Toast.LENGTH_LONG).show();
                }

                newPoints = new ArrayList<>();
                locationNames = new ArrayList<>();
                routeName.setText("");
                newLocation.setText("");
                adapter.notifyDataSetChanged();
            }
        });
    }
}