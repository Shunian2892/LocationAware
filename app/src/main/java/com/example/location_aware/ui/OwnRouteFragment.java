package com.example.location_aware.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.location_aware.R;
import com.example.location_aware.data.Data;
import com.example.location_aware.logic.OpenStreetMaps;
import com.example.location_aware.logic.routeRecyclerView.RouteAdapter;
import com.example.location_aware.logic.routeRecyclerView.*;
import com.google.gson.Gson;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

/**
 * In this class, the user can make his own route based on given locations. Before adding the location to the locations list, GeoPoint will check if it's a valid location.
 * If the list is complete and the user clicks on the create route button, a new Route object with the correct geopoints will be made and put in the recyclerviewer in the routes screen.
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
        private RouteAdapter routeAdapter;

        private FragmentManager fragmentManager;
        private SettingsFragmentButtons settingsFragmentButtons;
        private ImageButton ibBackButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_own_route, container, false);

        //Find all views
        fragmentManager = getFragmentManager();
        ibBackButton = v.findViewById(R.id.own_route_backButton);

        streetMaps = Data.getInstance().getStreetMaps();
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

        settingsFragmentButtons = Data.getInstance().getSettingsButtons();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Add a location to the locations list
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = newLocation.getText().toString();
                newGeoPoint = streetMaps.createGeoPoint(getContext(), location);

                //Check if location is a valid GeoPoint
                if(newGeoPoint == null){
                    makeToast(R.string.toast_valid_location);
                } else {
                    newPoints.add(newGeoPoint);
                    locationNames.add(location);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        //Delete previously added location from the list
        deleteLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check if locations added isn't smaller than 2 locations
                if(locationNames.size() != 0 && newPoints.size() != 0){
                    locationNames.remove(locationNames.size()-1);
                    newPoints.remove(newPoints.size()-1);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        //Create a new route based on the given locations and route name
        createRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = routeName.getText().toString();
                //Check if route name textfield isn't empty
                if(!name.equals("")){
                    if(newPoints.size() >= 2){
                        Route route = new Route(name, newPoints);
                        Data.getInstance().addRoute(route);
                        Data.getInstance().addToRouteHashMap(route.getName(), locationNames);
                        routeAdapter.notifyDataSetChanged();
                        makeToast(R.string.toast_ownRoute_created);
                    } else {
                        makeToast(R.string.toast_ownRoute_multiple_locations);
                    }
                } else {
                    makeToast(R.string.toast_ownRoute_name);
                }

                //Save data to shared preferences
                saveData();
                newPoints = new ArrayList<>();
                locationNames = new ArrayList<>();
                routeName.setText("");
                newLocation.setText("");
                adapter.notifyDataSetChanged();
            }
        });

        //Go back to the buttons fragment
        ibBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction().show(settingsFragmentButtons).commit();
                fragmentManager.beginTransaction().hide(Data.getInstance().getOwnRouteFragment()).commit();
            }
        });
    }

    /**
     * Save shared preferences data
     */
    public void saveData(){
        //Save route list to shared preferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonList = gson.toJson(Data.getInstance().getRouteList());
        editor.putString("route list",jsonList);
        editor.apply();

        //Save location list of a route to shared preferences
        SharedPreferences hashMapPref = getActivity().getSharedPreferences("hashmap", Context.MODE_PRIVATE);
        SharedPreferences.Editor hmEditor = hashMapPref.edit();
        Gson hmGson = new Gson();
        String jsonNames = hmGson.toJson(Data.getInstance().getRouteHashMap());
        hmEditor.putString("location name list",jsonNames);
        hmEditor.apply();
    }

    /**
     * Make a new toast
     * @param messageID id of the string resource such that the text changes depending on the device language
     */
    private void makeToast(int messageID){
        Toast.makeText(getContext(), messageID, Toast.LENGTH_LONG).show();
    }
}