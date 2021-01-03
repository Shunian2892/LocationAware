package com.example.location_aware.firebase;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * This class stores the user' name, latitude, and longitude which it gets from the database
 */
public class User {

    private String latitude;
    private String longitude;
    private String name;
    private String nickname;

    public User() {
    }

    public User(String latitude, String longitude, String name, String nickname) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.nickname = nickname;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        result.put("name", name);
        result.put("nickname", nickname);

        return result;
    }
}
