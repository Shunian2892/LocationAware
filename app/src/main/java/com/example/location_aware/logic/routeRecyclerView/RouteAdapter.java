package com.example.location_aware.logic.routeRecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.location_aware.R;
import com.example.location_aware.data.Data;

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

        Route routeClicked = Data.getInstance().getRouteList().get(position);
        System.out.println("ROUTE LIST FROM DATA -- SIZE ~~~~~~~~~~~~~~~~~" + Data.getInstance().getRouteList().size());
        System.out.println("CLICKED ROUTE IN ROUTEADAPTER ~~~~~~~~~~~~~~~~" +  routeClicked);

        holder.routeName.setText(route.getName());
        holder.routeImage.setImageResource(R.drawable.location);

        if(route.equals(routeClicked)){
            System.out.println("ROUTES ARE EQUAL !!!!!!!!!!!!!! NAME: " + route.getName());
        }

        if(Data.getInstance().getRouteHashMap().containsKey(route.getName())){

            holder.places.setText(Data.getInstance().getRouteHashMap().get(route.getName()).toString());
            System.out.println("LOCATIONS ARE SHOWN!!!!!!!");
        } else {
            System.out.println("STILL NOT WORKING!!!!! LE SAD FACE");
        }

//        if(Data.getInstance().getRouteHashMap().get(route.getName()) == null){
//            System.out.println(Data.getInstance().getRouteHashMap().get(route.getName()));
//            holder.places.setText("");
//        } else {
//            holder.places.setText(Data.getInstance().getRouteHashMap().get(route.getName()).toString());
//        }
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }
}
