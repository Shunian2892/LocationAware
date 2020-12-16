package com.example.location_aware.RouteRecyclerView;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.location_aware.R;
import com.example.location_aware.setRoute;

import java.util.ArrayList;

public class RouteRV extends Fragment implements OnItemClickListener {
    private RecyclerView routeRv;
    private ArrayList<Route> routeList;
    private RouteAdapter routeAdapter;
    private RouteManager routeManager;
    private ViewGroup container;
    private Context context;
    private com.example.location_aware.setRoute setRoute;

    public RouteRV(Context context, setRoute setRoute){
        this.context = context;
        this.setRoute =setRoute;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.container = container;
        return inflater.inflate(R.layout.fragment_route_r_v, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        routeList = new ArrayList<>();
        this.routeRv = this.container.findViewById(R.id.route_rv);
        this.routeManager = new RouteManager();
        this.routeList = this.routeManager.getRouteList();
        this.routeAdapter = new RouteAdapter(this, this.routeList);
        this.routeRv.setLayoutManager( new LinearLayoutManager(this.context));
        this.routeRv.setAdapter(this.routeAdapter);

    }

    @Override
    public void OnItemClick(int clickedPosition) {
        Route route = routeList.get(clickedPosition);
        Toast.makeText(this.context,route.getStringPlaces(),Toast.LENGTH_LONG).show();
        setRoute.setRouteCoord(route);
    }
}