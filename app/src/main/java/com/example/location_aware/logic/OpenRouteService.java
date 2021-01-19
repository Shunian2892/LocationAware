package com.example.location_aware.logic;

import android.util.Log;

import com.example.location_aware.data.Data;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * This class handles the api request to the openstreetmaps site for getting directions form one location to another location
 */
public class OpenRouteService {
    private final String APIKEY = "5b3ce3597851110001cf624838f723c4a0944307a267699df56fb5e8";
    private OkHttpClient client;

    public boolean isConnected;

    private OpenStreetMaps openStreetMaps;
    private GeometryDecoder decoder;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public OpenRouteService() {
        this.client = new OkHttpClient();
        this.isConnected = false;
        this.openStreetMaps = new OpenStreetMaps();
        Data.getInstance().setStreetMaps(openStreetMaps);
        this.decoder = new GeometryDecoder();
        Connect();
    }

    private void Connect() {
        this.isConnected = true;
    }

    /**
     * Create a new get request
     * @param method method of moving: walking, cycling, or driving
     * @param url open route service url which consists of open route service website, method, api key, and start location and enc location
     * @return return the request
     */
    private Request createGetRequest(String method, String url) {
        Request request = new Request.Builder().url("\n" +
                "https://api.openrouteservice.org/v2/directions/" + method + "?api_key=" + this.APIKEY + url).build();
        return request;
    }

    /**
     * Get a route between two points
     * @param startPoint startpoint of route
     * @param endPoint endpoint of route
     * @param method moving method: walking, cycling, driving
     */
    public void getRoute(GeoPoint startPoint, GeoPoint endPoint, String method) {
        ArrayList<GeoPoint> points = new ArrayList<>();

        if (this.isConnected) {
            //Create a new get requests
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
                            //Unpack JSON response into saperate objects
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

                                //Draw route on map
                                openStreetMaps.drawRoute(points);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    /**
     * Create new post request based on the given method and json file
     * @param method driving, walking, cycling
     * @param json json file with the locations
     * @return request
     */
    private Request createPostRequest(String method, String json) {
        RequestBody requestBody = RequestBody.create(json, JSON);
        Request request = new Request.Builder().url("https://api.openrouteservice.org/v2/directions/" + method).
                post(requestBody).addHeader("Authorization", APIKEY).build();
        return request;
    }

    /**
     * Get the route between all GeoPints in an arraylist based on the given method and (optional) language
     * @param waypoints the geopoints between which a route must be drawn
     * @param method driving, walking, cycling
     * @param language language in which the directions will be given
     */
    public void getRoute(ArrayList<GeoPoint> waypoints, String method,String language) {
        if (this.isConnected) {
            ArrayList<GeoPoint> points = new ArrayList<>();
            double[][] coordinates = new double[waypoints.size()][2];

            for (int i = 0; i < waypoints.size(); i++) {
                coordinates[i][0] = waypoints.get(i).getLongitude();
                coordinates[i][1] = waypoints.get(i).getLatitude();
            }

            //Create new request
            client.newCall(createPostRequest(method, "{\"coordinates\":" + Arrays.deepToString(coordinates) + ",\"language\":\"" + language + "\"}"))

                    .enqueue(new Callback() {

                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.d("FAILURE", "In OnFailure() in getRoute() multiple");
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            //Unpack JSON response into separate objects
                            try {
                                JSONObject responseObject = new JSONObject(response.body().string());
                                Log.d("JSONORS", responseObject.toString());
                                JSONArray routesArray = responseObject.getJSONArray("routes");
                                JSONObject routes = (JSONObject) routesArray.get(0);
                                String geometry = routes.getString("geometry");
                                JSONArray coordinates = decoder.decodeGeometry(geometry, false);

                                for (int i = 0; i < coordinates.length(); i++) {
                                    JSONArray cordArray = (JSONArray) coordinates.get(i);
                                    double lat = cordArray.getDouble(0);
                                    double lng = cordArray.getDouble(1);
                                    GeoPoint point = new GeoPoint(lat, lng);
                                    points.add(point);
                                }
                                //Clear map in case there is already a route drawn, then draw the new route
                                openStreetMaps.clearRoute();
                                openStreetMaps.drawRoute(points);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }


}
