package com.example.location_aware;

import android.app.Activity;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

public class LANearby {

    private MessageListener messageListener;
    private Message message;
    private Activity activity;

    public LANearby(Activity activity){
        this.activity = activity;

        this.message = null;

        messageListener = new MessageListener() {
            @Override
            public void onFound(Message message) {
                Log.d("MESSAGE", "Found message: " + new String(message.getContent()));
            }

            @Override
            public void onLost(Message message) {
                Log.d("MESSAGE", "Lost sight of message: " + new String(message.getContent()));
            }
        };
    }

    public void setMessage(String message){
        this.message = new Message(message.getBytes());
    }

    public void startListening() {
        Nearby.getMessagesClient(activity).publish(message);
        System.out.println("LANearby startListening: ~~~~~~~~~~~~~~ " + message);
        Nearby.getMessagesClient(activity).subscribe(messageListener);
    }

    public void stopListening() {
        Nearby.getMessagesClient(activity).unpublish(message);
        System.out.println("LANearby stopListening: ~~~~~~~~~~~~~~ " + message);
        Nearby.getMessagesClient(activity).unsubscribe(messageListener);
    }

//    private void publish(String message) {
//        Log.i("PUBLISH", "Publishing message: " + message);
//        activeMessage = new Message(message.getBytes());
//        Nearby.getMessagesClient(activity).publish(activeMessage);
//    }
//
//    private void unpublish() {
//        Log.i("UNPUBLISH", "Unpublishing.");
//        if (activeMessage != null) {
//            Nearby.getMessagesClient(activity).unpublish(activeMessage);
//            activeMessage = null;
//        }
//    }
//
//    // Subscribe to receive messages.
//    private void subscribe() {
//        Log.i("SUBSCRIBE", "Subscribing.");
//        Nearby.getMessagesClient(activity).subscribe(messageListener);
//    }
//
//    //Unsubscribe to receive messages
//    private void unsubscribe() {
//        Log.i("UNSUBSCRIBE", "Unsubscribing.");
//        Nearby.getMessagesClient(activity).unsubscribe(messageListener);
//    }

}
