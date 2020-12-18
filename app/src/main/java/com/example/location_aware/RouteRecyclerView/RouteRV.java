package com.example.location_aware.RouteRecyclerView;

import android.content.Context;
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
import android.widget.Toast;

import com.example.location_aware.Data;
import com.example.location_aware.OpenStreetMaps;
import com.example.location_aware.R;

import org.osmdroid.config.Configuration;

import java.util.ArrayList;

public class RouteRV extends Fragment implements OnItemClickListener {
    private RecyclerView routeRv;
    private ArrayList<Route> routeList;
    private RouteAdapter routeAdapter;
    private RouteManager routeManager;
    private ViewGroup container;
    private Context context;
    private SetRoute setRoute;

    public void setRoute(SetRoute setRoute){
        this.setRoute = setRoute;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        View v = inflater.inflate(R.layout.fragment_route_r_v, container, false);
        routeList = new ArrayList<>();
        this.routeRv = v.findViewById(R.id.route_rv);
        this.routeManager = new RouteManager();
        Data.getInstance().setRouteManager(routeManager);
        this.routeList = this.routeManager.getRouteList();
        this.routeAdapter = new RouteAdapter(this, this.routeList);
        Data.getInstance().setRouteAdapter(routeAdapter);
        this.routeRv.setLayoutManager(new LinearLayoutManager(this.context));
        this.routeRv.setAdapter(this.routeAdapter);
        return v;
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