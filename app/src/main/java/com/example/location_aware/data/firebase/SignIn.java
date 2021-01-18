package com.example.location_aware.data.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.location_aware.ui.LoginScreen;
import com.example.location_aware.ui.launcher.MainActivity;
import com.example.location_aware.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

/**
 * This class handles the signing in of a existing (firebase) user. Based on the given email address and password, the firebase authenticator will check if there is a user registered.
 * If not, it will show a toast with the error message.
 */
public class SignIn extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText email, password;
    private Button signIn, forgotPassword;
    private ProgressBar progressBar;
    private ImageButton backButton;

    private final String TAG = "SIGN IN CLASS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email = findViewById(R.id.email_address_sign_in);
        password = findViewById(R.id.password_sign_in);
        signIn = findViewById(R.id.sign_in_user_button);
        forgotPassword = findViewById(R.id.sign_in_forgot_password);
        progressBar = findViewById(R.id.sign_in_progress_bar);
        backButton = findViewById(R.id.sign_in_backButton);

        //Instantiate Firebase Authenticator and listener
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

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = email.getText().toString();
                String pass = password.getText().toString();
                //Show progressbar
                progressBar.setVisibility(View.VISIBLE);

                //Check if fields aren't empty
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
                                    makeToast(R.string.toast_signIn_invalid_email);
                                } catch (FirebaseAuthInvalidCredentialsException invalidPassword) {
                                    makeToast(R.string.toast_signIn_invalid_password);
                                } catch (Exception e) {
                                    Log.d(TAG, "onComplete: " + e.getMessage());
                                }
                            } else {
                                //Check if email is verified
                                if(!auth.getCurrentUser().isEmailVerified()){
                                    makeToast(R.string.toast_signIn_verify_email);
                                } else {
                                    makeToast(R.string.toast_logged_in);
                                    //Go to main activity
                                    Intent goToMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(goToMainActivity);

                                    //Close Sign in activity
                                    finish();
                                }
                            }
                        }
                    });
                } else  {
                    makeToast(R.string.toast_register_new_fields);
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToMainActivity = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(goToMainActivity);

                //Close Sign in activity
                finish();
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