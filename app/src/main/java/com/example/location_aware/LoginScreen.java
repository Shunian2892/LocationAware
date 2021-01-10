package com.example.location_aware;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.location_aware.firebase.Registration;
import com.example.location_aware.firebase.SignIn;

/**
 * This class only has two buttons, one for logging in and one for making a new account
 */
public class LoginScreen extends AppCompatActivity {
    private Button register, signIn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_screen);

        register = findViewById(R.id.login_register);
        signIn = findViewById(R.id.login_sign_in);

        onButtonClick();
    }

    /**
     * OnClick actions for the buttons
     */
    private void onButtonClick() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToRegisterScreen = new Intent(getApplicationContext(), Registration.class);
                startActivity(goToRegisterScreen);
                finish();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToSignInScreen = new Intent(getApplicationContext(), SignIn.class);
                startActivity(goToSignInScreen);
                finish();
            }
        });
    }
}
