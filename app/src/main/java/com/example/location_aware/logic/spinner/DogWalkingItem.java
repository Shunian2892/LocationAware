package com.example.location_aware.logic.spinner;

import org.osmdroid.util.GeoPoint;

public class DogWalkingItem {
    private String name;
    private int image;
    private GeoPoint location;

    public DogWalkingItem(String name, int image, GeoPoint location) {
        this.name = name;
        this.image = image;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }
}
