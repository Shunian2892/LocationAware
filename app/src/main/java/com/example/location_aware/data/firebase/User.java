package com.example.location_aware.data.firebase;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * This class stores the user' name, latitude, and longitude which it gets from the database
 */
public class User {

    private double latitude;
    private double longitude;
    private String name;

    public User() {
    }

    public User(double latitude, double longitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        result.put("name", name);

        return result;
    }
}
