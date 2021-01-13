package com.example.location_aware.logic;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

public class MapHelperTest extends TestCase {
    private MapHelper mapHelper;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        mapHelper = new MapHelper();
    }

    @Test
    public void testDegreesToRadians() {
        double degrees = 200;
        double testRadians =  mapHelper.degreesToRadians(degrees);
        assertEquals(testRadians, 3.490658503988659);
    }

    @Test
    public void testDistanceCoords() {
        double lat1 = 51.593940;
        double lon1 = 4.779280;
        double lat2 = 51.594340;
        double lon2 = 4.779160;

        double expected = mapHelper.distanceCoords(lat1, lon1, lat2, lon2);
        assertEquals(expected, 45, 0.5);
    }
}