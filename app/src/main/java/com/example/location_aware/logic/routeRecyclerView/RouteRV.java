package com.example.location_aware.logic.routeRecyclerView;

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
import android.widget.Button;

import com.example.location_aware.data.Data;
import com.example.location_aware.R;

import org.osmdroid.config.Configuration;

import java.util.ArrayList;

/**
 * This class handles the recycler viewer in which all saved routes are shown
 */
public class RouteRV extends Fragment implements OnItemClickListener {
    private RecyclerView routeRv;
    private ArrayList<Route> routeList;
    private RouteAdapter routeAdapter;
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

        routeList = Data.getInstance().getRouteList();

        this.routeRv = v.findViewById(R.id.route_rv);
        this.routeAdapter = new RouteAdapter(this, this.routeList);
        Data.getInstance().setRouteAdapter(routeAdapter);
        this.routeRv.setLayoutManager(new LinearLayoutManager(this.context));
        this.routeRv.setAdapter(this.routeAdapter);

        return v;
    }

    /**
     * Get the correct data from the clicked item and show it on the map.
     * @param clickedPosition
     */
    @Override
    public void OnItemClick(int clickedPosition) {
        Route route = routeList.get(clickedPosition);
        Data.getInstance().getStreetMaps().clearRoute();
        Data.getInstance().setRoute(route);
        //setRoute.setRouteCoord(route);
    }
}