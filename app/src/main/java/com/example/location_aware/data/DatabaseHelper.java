package com.example.location_aware.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.location_aware.ui.MapFragment;
import com.example.location_aware.data.firebase.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DatabaseHelper {
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private FirebaseAuth auth;
    private HashMap<String, Object> userNameAndLocation;
    private User user;
    private String userPathSubstring;

    public DatabaseHelper() {
        userNameAndLocation = new HashMap<>();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    /**
     * Get the current user information from the Database and go into this users subbranch
     */
    public String getCurrentUserDatabase() {
        userPathSubstring = Data.getInstance().getCurrentUser();

        //Go to the subbranch of this specific user
        dbRef = database.getReference("Location Aware").child("User").child(userPathSubstring);
        userNameAndLocation.put("name", userPathSubstring);

        return userPathSubstring;
    }

    /**
     * Update current user values (longitude and latitude) in the database
     */
    public HashMap<String, Object> updateUserValues() {
        double latitude = Data.getInstance().getCurrentLocation().getLatitude();
        double longitude = Data.getInstance().getCurrentLocation().getLongitude();

        userNameAndLocation.put("latitude", latitude);
        userNameAndLocation.put("longitude", longitude);

        dbRef.updateChildren(userNameAndLocation);

        return userNameAndLocation;
    }

    /**
     * Get the data from the database with all the users
     */
    public User getDbData(){
        MapFragment mapFragment = Data.getInstance().getMapFragment();

        DatabaseReference getDataRef = database.getReference("Location Aware").child("User");
        getDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot usersSnapshot : snapshot.getChildren()){

                    user = usersSnapshot.getValue(User.class);

                    if(mapFragment != null){
                        if(!user.getName().equals(Data.getInstance().getCurrentUser())){
                            Data.getInstance().getMarkerUpdateListener().onMarkerUpdate(user);
                        }
                    } else {
                        Log.d("DATABASEHELPER", "mapfragment is null");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return user;
    }
}
