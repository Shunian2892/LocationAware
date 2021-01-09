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

import com.example.location_aware.Data;
import com.example.location_aware.MainActivity;
import com.example.location_aware.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText email, password;
    private Button signIn;
    private ProgressBar progressBar;

    private final String TAG = "SIGN IN CLASS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email = findViewById(R.id.email_address_sign_in);
        password = findViewById(R.id.password_sign_in);
        signIn = findViewById(R.id.sign_in_user_button);
        progressBar = findViewById(R.id.sign_in_progress_bar);

        //Instantiate Firebase Authenticator and listener
        auth = FirebaseAuth.getInstance();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = email.getText().toString();
                String pass = password.getText().toString();
                //Show progressbar
                progressBar.setVisibility(View.VISIBLE);

                if(!emailAddress.equals("") && !pass.equals("")){
                    auth.signInWithEmailAndPassword(emailAddress, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Hide progressbar
                            progressBar.setVisibility(View.GONE);
                            if(!task.isSuccessful()){
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidUserException invalidEmail) {
//                                    Log.d(TAG, "onComplete: Invalid email");
                                    makeToast("Invalid/non existing email address!");
                                } catch (FirebaseAuthInvalidCredentialsException invalidPassword) {
//                                    Log.d(TAG, "onComplete: Invalid password!");
                                    makeToast("Invalid password");
                                } catch (Exception e) {
                                    Log.d(TAG, "onComplete: " + e.getMessage());
                                }
                            } else {
                                //Go to main activity
                                Intent goToMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(goToMainActivity);

                                //Close Sign in activity
                                finish();
                            }
                        }
                    });
                } else  {
                    makeToast("Please fill in all the fields");
                }
            }
        });
    }

    private void makeToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}