package com.example.location_aware;

import androidx.annotation.NonNull;

import com.example.location_aware.firebase.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class DatabaseHelper {
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private FirebaseAuth auth;
    private HashMap<String, Object> userNameAndLocation;
    private User user;
    private String[] currentUser;
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
//
//        if(auth.getCurrentUser() != null){
//            //Get the email address of the current user and split it
//            currentUser = auth.getCurrentUser().getEmail().split(Pattern.quote("@"));
//            userPathSubstring = currentUser[0];
//
//
//        } else {
//            return null;
//        }

        System.out.println("CURRENT USER IN DATA CLASS ~~~~~~~~~~~~~~~~~~~~~ " + Data.getInstance().getCurrentUser());
        System.out.println("CURRENT USER IN USERPATHSUBSTRING VARIABLE ~~~~~~~~~~~~~~~~~~~~~ " + userPathSubstring);
        userPathSubstring = Data.getInstance().getCurrentUser();
        //Go to the subbranch of this specific user
        dbRef = database.getReference("Location Aware").child("User").child(userPathSubstring);
        userNameAndLocation.put("name", userPathSubstring);
        System.out.println("USERNAME FROM EMAILADDRESS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ " + userPathSubstring);
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
        //getDbData();

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

                    System.out.println("USERSNAPSHOT ~~~~~~~~~~~~~~~~~~~~~~~~~~~" + usersSnapshot);
                    user = usersSnapshot.getValue(User.class);
                    System.out.println("USER FROM USERSNAPSHOT ~~~~~~~~~~~~~~~~~~~~~~~" + user);

                    if(!user.getName().equals(Data.getInstance().getCurrentUser())){
                        mapFragment.drawOtherUser(user);
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
