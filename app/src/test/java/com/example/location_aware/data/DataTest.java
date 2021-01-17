package com.example.location_aware.data;

import com.example.location_aware.logic.routeRecyclerView.Route;

import junit.framework.TestCase;

import org.junit.Before;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class DataTest extends TestCase {
    private Data data;
    private ArrayList<GeoPoint> locations;
    private Route testRoute;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        data = Data.getInstance();
        locations = new ArrayList<>();
        locations.add(new GeoPoint(51.594340, 4.779160) );
        locations.add(new GeoPoint(51.593941, 4.779280) );
        testRoute = new Route("testRoute", locations);
    }

    public void testGetCurrentLocation() {
        GeoPoint currentLocation = new GeoPoint(51.594340, 4.779160);
        data.setCurrentLocation(currentLocation);
        assertEquals(currentLocation, data.getCurrentLocation());
    }

    public void testSetCurrentLocation() {
        GeoPoint currentLocationOld = new GeoPoint(51.594340, 4.779160);
        data.setCurrentLocation(currentLocationOld);
        assertEquals(currentLocationOld, data.getCurrentLocation());

        GeoPoint currentLocationNew = new GeoPoint(51.593941, 4.779280);
        data.setCurrentLocation(currentLocationNew);
        assertEquals(currentLocationNew, data.getCurrentLocation());
    }

    public void testGetRouteMethod() {
        String method = "cycling-regular";
        data.setRouteMethod(method);
        assertEquals(method, data.getRouteMethod());
    }

    public void testSetRouteMethod() {
        data.setRouteMethod("foot-walking");
        assertEquals("foot-walking", data.getRouteMethod());
        data.setRouteMethod("cycling-regular");
        assertEquals("cycling-regular", data.getRouteMethod());
    }

    public void testGetRoute() {
        data.setRoute(testRoute);
        assertEquals(testRoute, data.getRoute());
    }

    public void testSetRoute() {
        data.setRoute(testRoute);
        assertEquals(testRoute, data.getRoute());
    }

    public void testGetCurrentUser() {
        String currentUser = "Test user";
        data.setCurrentUser(currentUser);
        assertEquals(currentUser, data.getCurrentUser());
    }

    public void testSetCurrentUser() {
        String currentUser1 = "Test user";
        data.setCurrentUser(currentUser1);
        assertEquals(currentUser1, data.getCurrentUser());

        String currentUser2 = "Other test user";
        data.setCurrentUser(currentUser2);
        assertEquals(currentUser2, data.getCurrentUser());
    }
}