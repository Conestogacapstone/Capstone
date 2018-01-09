package com.example.android.capstone_project.Data;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.capstone_project.Authenticator.Login;
import com.example.android.capstone_project.Authenticator.signUp;
import com.example.android.capstone_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class myProfile extends AppCompatActivity {

    private Button Emailchange_button, Passwordchange_button, UserRemove_button, Mailchange_button, Passchange_button, signOut_button, ViewProfile_button;
    private EditText mailold, mailnew, password, Passnew;
    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener mListenerauth;
    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //This is the view
        setContentView(R.layout.activity_my_profile);


        // Add back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Instance of Firebase
        mauth = FirebaseAuth.getInstance();

        //getting current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mListenerauth = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(myProfile.this, Login.class));
                    finish();
                }
            }
        };

        mailold.setVisibility(View.GONE);// Old mail visibility gone
        mailnew.setVisibility(View.GONE);// new mail visibility gone
        password.setVisibility(View.GONE);//password visibility gone
        Passnew.setVisibility(View.GONE);// new password visibility gone
        Mailchange_button.setVisibility(View.GONE);//mail_change visibility gone
        Passchange_button.setVisibility(View.GONE);//pass_change visibility gone

        Emailchange_button = (Button) findViewById(R.id.emailchange_button);// Getting Emailchange button
        Passwordchange_button = (Button) findViewById(R.id.passchange_button);// Getting Passwordchange button
        UserRemove_button = (Button) findViewById(R.id.userremove_button);// Getting Userremove button
        Mailchange_button = (Button) findViewById(R.id.email_change);// Getting Emailchange button
        Passchange_button = (Button) findViewById(R.id.Pass_change);// Getting Passchange button
        signOut_button = (Button) findViewById(R.id.signout);// Getting Signout button
        ViewProfile_button = (Button) findViewById(R.id.viewUser);// Getting Viewprofile button
        mailold = (EditText) findViewById(R.id.emailold);// Getting Emailchange from text view
        mailnew = (EditText) findViewById(R.id.emailnew);// Getting NewEmail from text view
        password = (EditText) findViewById(R.id.password);// Getting password from textview
        Passnew = (EditText) findViewById(R.id.Pass_new);// Getting newpass from textview

        progressBar = (ProgressBar) findViewById(R.id.progressBar);// Getting progressBar

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        //This method will change the visibility of text fields
        Emailchange_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mailold.setVisibility(View.GONE);
                mailnew.setVisibility(View.VISIBLE);
                password.setVisibility(View.GONE);
                Passnew.setVisibility(View.GONE);
                Mailchange_button.setVisibility(View.VISIBLE);
                Passchange_button.setVisibility(View.GONE);
            }
        });

        //This method will change the email
        Mailchange_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (user != null && !mailnew.getText().toString().trim().equals("")) {
                    user.updateEmail(mailnew.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(myProfile.this, "Your Email address is changed", Toast.LENGTH_LONG).show();
                                        signOut();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(myProfile.this, "Sorry, Your Email address was not changed", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                } else if (mailnew.getText().toString().trim().equals("")) {
                    mailnew.setError("Please Enter your email");
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        //This method will change the visibility of textfields
        Passwordchange_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mailold.setVisibility(View.GONE);
                mailnew.setVisibility(View.GONE);
                password.setVisibility(View.GONE);
                Passnew.setVisibility(View.VISIBLE);
                Mailchange_button.setVisibility(View.GONE);
                Passchange_button.setVisibility(View.VISIBLE);
            }
        });

        //This method will change the password
        Passchange_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (user != null && !Passnew.getText().toString().trim().equals("")) {
                    if (Passnew.getText().toString().trim().length() < 6) {
                        Passnew.setError("Please Enter minimum of 6 characters in your password field");
                        progressBar.setVisibility(View.GONE);
                    } else {
                        user.updatePassword(Passnew.getText().toString().trim())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(myProfile.this, "Password is updated, sign in with new password", Toast.LENGTH_SHORT).show();
                                            signOut();
                                            progressBar.setVisibility(View.GONE);
                                        } else {
                                            Toast.makeText(myProfile.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                    }
                } else if (Passnew.getText().toString().trim().equals("")) {
                    Passnew.setError("Enter password");
                    progressBar.setVisibility(View.GONE);
                }
            }
        });


        //This method will remove the User
        UserRemove_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (user != null) {
                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(myProfile.this, "Your profile is deleted:( Create a account now!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(myProfile.this, signUp.class));
                                        finish();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(myProfile.this, "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                }
            }
        });

        //This method will sign out the user
        signOut_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        //This method will view the profile of user
        ViewProfile_button.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myProfile.this, viewUser.class);
                startActivity(intent);
            }
        });


    }

    //This method will sign out the user
    public void signOut() {
        mauth.signOut();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        mauth.addAuthStateListener(mListenerauth);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mListenerauth != null) {
            mauth.removeAuthStateListener(mListenerauth);
        }
    }
}

