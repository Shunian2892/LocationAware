package com.example.location_aware.firebase;


import com.example.location_aware.Data;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Database {
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    public Database(){
        //Initialise Firebase database
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("Location Aware");
    }

    public void goToUidChild(String childBranch){
        dbRef.child("User").child(childBranch);
    }

    public void setValue(String Uid, String value){
        dbRef.child("User").child(Uid).setValue(value);
    }
}
