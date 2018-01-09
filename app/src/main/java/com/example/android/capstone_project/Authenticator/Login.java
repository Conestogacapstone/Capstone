package com.example.android.capstone_project.Authenticator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.capstone_project.Activities.MainActivity;
import com.example.android.capstone_project.R;
import com.example.android.capstone_project.Data.resetPassword;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private EditText Email, Password;
    private FirebaseAuth mauth;
    private ProgressBar progressBar;
    private Button Signup_button, Login_button, Reset_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Instance of Firebase
        mauth = FirebaseAuth.getInstance();


        //Automatic Login if user is already signed in
        if (mauth.getCurrentUser() != null) {

            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
            toastMessage("Successfully signed in with: " + mauth.getCurrentUser().getEmail());

        }


        // This is the view
        setContentView(R.layout.activity_login);


        Email = (EditText) findViewById(R.id.inputemail); // Getting user's email to Email
        Password = (EditText) findViewById(R.id.inputpassword); // Getting user's password to Password
        progressBar = (ProgressBar) findViewById(R.id.progressBar); // Getting progressBar
        Signup_button = (Button) findViewById(R.id.signup); // Getting Signup button
        Login_button = (Button) findViewById(R.id.login); // Getting Login button
        Reset_button = (Button) findViewById(R.id.resetpassword); // Getting reset button

        //Reset button to move to resetPassword page
        Reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, resetPassword.class));
            }
        });

        //Signup button to move to registration page
        Signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, signUp.class));
            }
        });


        // This is the Login button
        Login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email.getText().toString();
                final String password = Password.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Please Enter your email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Please Enter your password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Visibility of progress bar is set to visible
                progressBar.setVisibility(View.VISIBLE);


                //This method will give authentication to user
                mauth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // This method will display a message to the user if Login fails.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    if (password.length() < 6) {
                                        Password.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(Login.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                }
                                //If Log in is successful then it will redirect user to Main page.
                                else {

                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    // This is the toast message method
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
