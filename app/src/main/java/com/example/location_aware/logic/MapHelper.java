package com.example.location_aware.logic;

public class MapHelper {
    public double degreesToRadians(double degrees){
        return degrees*Math.PI/180;
    }

    public double distanceCoords(double lat1, double lon1, double lat2, double lon2){
        int earthRadius = 6371;

        double dLat = degreesToRadians(lat2-lat1);
        double dLon = degreesToRadians(lon2-lon1);

        double rLat1 = degreesToRadians(lat1);
        double rLat2 = degreesToRadians(lat2);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(rLat1)*Math.cos(rLat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return earthRadius * c * 1000;
    }
}

