package com.example.location_aware.ui.launcher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.example.location_aware.ui.LoginScreen;
import com.example.location_aware.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ImageView animation = findViewById(R.id.splashAnimation);
        AnimationDrawable splashAnimation = (AnimationDrawable) animation.getBackground();
        splashAnimation.run();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, LoginScreen.class));
                finish();
            }
        }, 3000);
    }
}