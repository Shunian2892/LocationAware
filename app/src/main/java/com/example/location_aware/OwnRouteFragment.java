package com.example.location_aware;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.google.gson.Gson;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;


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
                        Route route = new Route(name, newPoints);
                        Data.getInstance().addRoute(route);
                        Data.getInstance().addToRouteHashMap(route.getName(), locationNames);
                        routeAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Route is created!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Please add multiple location!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Please type in a name!", Toast.LENGTH_LONG).show();
                }

                saveData();
                newPoints = new ArrayList<>();
                locationNames = new ArrayList<>();
                routeName.setText("");
                newLocation.setText("");
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void saveData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonList = gson.toJson(Data.getInstance().getRouteList());
        editor.putString("route list",jsonList);
        editor.apply();

        SharedPreferences hashMapPref = getActivity().getSharedPreferences("hashmap", Context.MODE_PRIVATE);
        SharedPreferences.Editor hmEditor = hashMapPref.edit();
        Gson hmGson = new Gson();
        String jsonNames = hmGson.toJson(Data.getInstance().getRouteHashMap());
        hmEditor.putString("location name list",jsonNames);
        hmEditor.apply();
    }
}