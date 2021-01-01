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
import android.widget.Toast;

import com.example.location_aware.MainActivity;
import com.example.location_aware.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registration extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private EditText newEmail, newPassword;
    private Button register;
    private ProgressBar progressBar;

    private final String TAG = "REGISTRATION CLASS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        register = findViewById(R.id.register_new_user_button);
        newEmail = findViewById(R.id.email_address_registration);
        newPassword = findViewById(R.id.password_registration);
        progressBar = findViewById(R.id.registration_progress_bar);

        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email = newEmail.getText().toString();
                String password = newPassword.getText().toString();

                //Check if fields are filled in
                if (!email.equals("") && !password.equals("")) {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if(task.isSuccessful()){
                                makeToast("Registration successful!");
                                Log.d(TAG, "createUserWithEmail:success");
                                newEmail.setText("");
                                newPassword.setText("");
                                //Go to main activity
                                Intent goToMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(goToMainActivity);
                                //Close Registration activity
                                finish();
                            } else {
                                makeToast(task.getException().getMessage());
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            }
                        }
                    });
                } else {
                    makeToast("Please fill in all the fields");
                }
            }
        });
    }

    private void makeToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}