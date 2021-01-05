package com.example.location_aware.RouteRecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.location_aware.Data;
import com.example.location_aware.OpenStreetMaps;
import com.example.location_aware.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.osmdroid.config.Configuration;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class RouteRV extends Fragment implements OnItemClickListener {
    private RecyclerView routeRv;
    private ArrayList<Route> routeList;
    private RouteAdapter routeAdapter;
    private RouteManager routeManager;
    private ViewGroup container;
    private Context context;
    private SetRoute setRoute;
    private Button saveButton;

    public void setRoute(SetRoute setRoute){
        this.setRoute = setRoute;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        View v = inflater.inflate(R.layout.fragment_route_r_v, container, false);

        routeList = Data.getInstance().getRouteList();


      /*  this.routeManager = new RouteManager();
        Data.getInstance().setRouteManager(routeManager);*/
        //routeList = this.routeManager.getRouteList();

        this.routeRv = v.findViewById(R.id.route_rv);
        this.routeAdapter = new RouteAdapter(this, this.routeList);
        Data.getInstance().setRouteAdapter(routeAdapter);
        this.routeRv.setLayoutManager(new LinearLayoutManager(this.context));
        this.routeRv.setAdapter(this.routeAdapter);

        this.saveButton = v.findViewById(R.id.save_button);
        this.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
        return v;
    }

    public void saveData(){

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonList = gson.toJson(routeList);
        editor.putString("route list",jsonList);
        editor.apply();

        SharedPreferences hashMapPref = getActivity().getSharedPreferences("hashmap", Context.MODE_PRIVATE);
        SharedPreferences.Editor hmEditor = hashMapPref.edit();
        Gson hmGson = new Gson();
        String jsonNames = hmGson.toJson(Data.getInstance().getRouteHashMap());
        hmEditor.putString("location name list",jsonNames);
        hmEditor.apply();
    }

    @Override
    public void OnItemClick(int clickedPosition) {
        Route route = routeList.get(clickedPosition);
        Data.getInstance().getStreetMaps().clearRoute();
        Data.getInstance().setRoute(route);
//        Toast.makeText(this.context,route.getStringPlaces(),Toast.LENGTH_LONG).show();
        setRoute.setRouteCoord(route);
    }
}