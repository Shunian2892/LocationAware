package com.example.location_aware.data.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.location_aware.R;
import com.example.location_aware.ui.LoginScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * This class handles the request of creating a new user in the database. If registration fails, it will show a toast message with the reason.
 */
public class Registration extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText newEmail, newPassword;
    private Button register;
    private ProgressBar progressBar;
    private ImageButton backButton;

    private final String TAG = "REGISTRATION CLASS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        register = findViewById(R.id.register_new_user_button);
        newEmail = findViewById(R.id.email_address_registration);
        newPassword = findViewById(R.id.password_registration);
        progressBar = findViewById(R.id.registration_progress_bar);
        backButton = findViewById(R.id.registration_backButton);

        //Get Firebase authenticator
        auth = FirebaseAuth.getInstance();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToLogIn = new Intent(getApplicationContext(), LoginScreen.class);
                startActivity(goToLogIn);

                //Close Registration activity
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show progressbar as an indicator that the user is being added to the database
                progressBar.setVisibility(View.VISIBLE);
                String email = newEmail.getText().toString();
                String password = newPassword.getText().toString();

                //Check if fields are filled in
                if (!email.equals("") && !password.equals("")) {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Hide progressbar after registration completion
                            progressBar.setVisibility(View.GONE);
                            if(task.isSuccessful()){
                                //Send verification email to user
                                auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            makeToast(R.string.toast_register_new);
                                            newEmail.setText("");
                                            newPassword.setText("");

                                            //Go to main activity
                                            Intent goToSignIn = new Intent(getApplicationContext(), SignIn.class);
                                            startActivity(goToSignIn);

                                            //Close Registration activity
                                            finish();
                                        } else {
                                            makeToast(R.string.toast_register_new_failed);
                                        }
                                    }
                                });
                            } else {
                                makeToast(R.string.toast_register_new_failed);
                            }
                        }
                    });
                } else {
                    makeToast(R.string.toast_register_new_fields);
                }
            }
        });
    }

    /**
     * Make a new toast
     * @param message id of the string resource such that the text changes depending on the device language
     */
    private void makeToast(int message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}