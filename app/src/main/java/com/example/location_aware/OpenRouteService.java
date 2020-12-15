package com.example.location_aware;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OpenRouteService {
    private final String apiKey = "5b3ce3597851110001cf624838f723c4a0944307a267699df56fb5e8";
    private OkHttpClient client;
    private String ipAddress;
    private int port;
    public boolean isConnected;

    private OpenStreetMaps openStreetMaps;
    private MapView mapView;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public OpenRouteService(MapView mapView) {
        this.client = new OkHttpClient();
        this.ipAddress = "localhost";
        this.port = 8000;
        this.isConnected = false;
        this.openStreetMaps = new OpenStreetMaps();
        this.mapView = mapView;

        Connect();
    }

    private void Connect() {
        this.isConnected = true;
    }

    private Request createGetRequest(String method, String url) {
        Request request = new Request.Builder().url("\n" +
                "https://api.openrouteservice.org/v2/directions/" + method + "?api_key=" + this.apiKey + url).build();
        return request;
    }

    public void getRoute(GeoPoint startPoint, GeoPoint endPoint, String method) {
        ArrayList<GeoPoint> points = new ArrayList<>();

        if (this.isConnected) {
            client.newCall(createGetRequest(method,
                    "&start=" + startPoint.getLongitude() + "," + startPoint.getLatitude()
                            + "&end=" + endPoint.getLongitude() + "," + endPoint.getLatitude()))
                    .enqueue(new Callback() {

                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.d("FAILURE", "In OnFailure() in example()");
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            try {
                                JSONObject responseObject = new JSONObject(response.body().string());
                                JSONArray featureArray = responseObject.getJSONArray("features");
                                JSONObject feature = (JSONObject) featureArray.get(0);
                                JSONObject geometry = feature.getJSONObject("geometry");
                                JSONArray coordinates = geometry.getJSONArray("coordinates");

                                for (int i = 0; i < coordinates.length(); i++) {
                                    JSONArray coordArray = (JSONArray) coordinates.get(i);
                                    double lng = coordArray.getDouble(0);
                                    double lat = coordArray.getDouble(1);
                                    GeoPoint point = new GeoPoint(lat, lng);
                                    points.add(point);
                                }

                                openStreetMaps.drawRoute(mapView, points);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }
}
