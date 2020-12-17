package com.example.location_aware.RouteRecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.location_aware.R;

class RouteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView routeName;
    public ImageView routeImage;
    public OnItemClickListener clickListener;

    public RouteViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        routeImage = itemView.findViewById(R.id.route_list_item_image);
        routeName = itemView.findViewById(R.id.route_list_item_name);
        clickListener = listener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        clickListener.OnItemClick(getAdapterPosition());
        Log.d("ROUTEVH clicklistener", getAdapterPosition()+"");
    }
}
