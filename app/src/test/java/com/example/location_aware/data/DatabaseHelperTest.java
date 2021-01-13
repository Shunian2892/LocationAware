package com.example.location_aware.data;

import android.content.Context;

import com.example.location_aware.ui.launcher.MainActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

public class DatabaseHelperTest extends TestCase {
    private DatabaseHelper dbHelper;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private Data data;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        dbHelper = new DatabaseHelper();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        data = Data.getInstance();
    }

    @Test
    public void testGetCurrentUserDatabase() {
        String testUserName = data.getCurrentUser();
        assertEquals(testUserName, dbHelper.getCurrentUserDatabase());
    }

    @Test
    public void testUpdateUserValues() {
    }

    @Test
    public void testGetDbData() {
    }
}