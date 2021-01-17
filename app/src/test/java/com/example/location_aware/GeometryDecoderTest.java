package com.example.location_aware;

import com.example.location_aware.logic.GeometryDecoder;
import com.google.gson.JsonArray;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GeometryDecoderTest {

    @Test
    public void geometryDecoderTest(){
        String encodedGeometryTest = "";

        JsonArray expected = new JsonArray();

        assertEquals(expected, GeometryDecoder.decodeGeometry(encodedGeometryTest, true));
    }
}