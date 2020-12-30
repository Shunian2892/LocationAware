package com.example.location_aware.RouteRecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.location_aware.Data;
import com.example.location_aware.R;

import java.util.ArrayList;

public class RouteAdapter extends RecyclerView.Adapter<RouteViewHolder> {
    private ArrayList<Route> routeList;
    private OnItemClickListener listener;

    public RouteAdapter(OnItemClickListener listener, ArrayList<Route> routeList){
        this.listener = listener;
        this.routeList = routeList;

    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_list_item,parent,false);
        return new RouteViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        Route route = this.routeList.get(position);
        holder.routeName.setText(route.getName());
        holder.routeImage.setImageResource(R.drawable.location);
        holder.places.setText(Data.getInstance().getRouteHashMap().get(route.getName()).toString());

    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }
}
