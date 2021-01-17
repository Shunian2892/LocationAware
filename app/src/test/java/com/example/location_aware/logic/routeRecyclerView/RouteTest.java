package com.example.location_aware.logic.routeRecyclerView;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class RouteTest extends TestCase {
    private Route route1, route2;
    private ArrayList<GeoPoint> testPoints;
    private String[] testLocations;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        testPoints = new ArrayList<>();
        testPoints.add(new GeoPoint(51.5943506, 4.7792963));
        testPoints.add(new GeoPoint(51.5502, 4.7590));
        route1 = new Route("testRoute1", testPoints);

        testLocations = new String[] { "breda station", "mastbos"};
        route2 = new Route("testRoute2", testLocations);
    }

    @Test
    public void testGetGeoPoints() {
        ArrayList<GeoPoint> getTestPoints = testPoints;

        assertEquals(getTestPoints, testPoints);
    }

    @Test
    public void testTestGetName() {
        String expected = "testRoute1";

        assertEquals(expected, route1.getName());
    }

    @Test
    public void testTestSetName() {
        String before = route1.getName();
        String after = "HELLOOOOO";

        assertEquals(before, route1.getName());
        route1.setName(after);
        assertEquals(after, route1.getName());
    }

    @Test
    public void testGetPlaces() {
        String[] getTestPlaces = route2.getPlaces();

        assertEquals(getTestPlaces, route2.getPlaces());
    }

    @Test
    public void testIsOwnMade() {
        assertEquals(true, route1.isOwnMade());
        assertEquals(false, route2.isOwnMade());
    }
}