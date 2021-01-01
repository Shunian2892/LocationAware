package com.example.location_aware.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.location_aware.MainActivity;
import com.example.location_aware.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private EditText email, password;
    private Button signIn;
    private ProgressBar progressBar;
    private Database database;

    private final String TAG = "SIGN IN CLASS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        database = new Database();
        email = findViewById(R.id.email_address_sign_in);
        password = findViewById(R.id.password_sign_in);
        signIn = findViewById(R.id.sign_in_user_button);
        progressBar = findViewById(R.id.sign_in_progress_bar);

        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //Get correct user through email validation
                FirebaseUser user = firebaseAuth.getCurrentUser();
//                progressBar.setVisibility(View.VISIBLE);
                if(user != null){
                    Log.d(TAG, "onAuthStateChanged: signed_in" + user.getUid());
                    makeToast("Successfully signed in!");
                }
            }
        };

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = email.getText().toString();
                String pass = password.getText().toString();

                if(!emailAddress.equals("") && !pass.equals("")){
                    auth.signInWithEmailAndPassword(emailAddress, pass);
                    //Go to main activity
                    Intent goToMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(goToMainActivity);
                    //Close Sign in activity
                    finish();
                } else  {
                    makeToast("Please fill in all the fields");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    private void makeToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}