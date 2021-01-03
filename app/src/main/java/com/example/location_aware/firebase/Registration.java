package com.example.location_aware.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.location_aware.Data;
import com.example.location_aware.MainActivity;
import com.example.location_aware.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private EditText newEmail, newPassword, newNickname;
    private Button register;
    private ProgressBar progressBar;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    private final String TAG = "REGISTRATION CLASS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        register = findViewById(R.id.register_new_user_button);
        newEmail = findViewById(R.id.email_address_registration);
        newPassword = findViewById(R.id.password_registration);
        newNickname = findViewById(R.id.nickname_registration);
        progressBar = findViewById(R.id.registration_progress_bar);

        //Get Firebase authenticator
        auth = FirebaseAuth.getInstance();
//        database = FirebaseDatabase.getInstance();
//        dbRef = database.getReference("Location Aware").child("User");

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show progressbar as an indicator that the user is being added to the database
                progressBar.setVisibility(View.VISIBLE);
                String email = newEmail.getText().toString();
                String password = newPassword.getText().toString();
                String nickname = newNickname.getText().toString();

                //Check if fields are filled in
                if ((!email.equals("") && !password.equals("") && !nickname.equals(""))) {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Hide progressbar after registration completion
                            progressBar.setVisibility(View.GONE);
                            if(task.isSuccessful()){
                                makeToast("Registration successful!");
                                Log.d(TAG, "createUserWithEmail:success");
                                System.out.println(nickname);
//
//                                Data.getInstance().setUserNickname(nickname);
//
//                                System.out.println("NICkNAME REGISTRATION ~~~~~~~~~~~~~~~ " + Data.getInstance().getUserNickname());

                                newEmail.setText("");
                                newPassword.setText("");
                                newNickname.setText("");

                                //Go to main activity
                                Intent goToMainActivity = new Intent(getApplicationContext(), MainActivity.class);
//                                goToMainActivity.putExtra("NICKNAME", nickname);
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

//    private void saveData(String nickname) {
//        SharedPreferences nicknamePrefs = getApplicationContext().getSharedPreferences("nickname", MODE_PRIVATE);
//        SharedPreferences.Editor editor = nicknamePrefs.edit();
//        editor.putString("userNickName", nickname);
//        editor.apply();
//    }

    private void makeToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}