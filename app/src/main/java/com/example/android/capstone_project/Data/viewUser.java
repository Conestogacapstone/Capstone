package com.example.android.capstone_project.Data;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.capstone_project.Authenticator.Login;
import com.example.android.capstone_project.R;
import com.google.firebase.auth.FirebaseAuth;


import com.google.firebase.auth.FirebaseUser;

public class viewUser extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private ImageView imageView;
    private TextView email;
    private TextView name;
    private View btnLogOut;
    private TextView userId;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        name = (TextView) findViewById(R.id.displayed_name);
        email = (TextView) findViewById(R.id.email_field);
        btnLogOut = findViewById(R.id.logout);
        userId = (TextView) findViewById(R.id.user_id);
        imageView = (ImageView) findViewById(R.id.user_photo);
        btnBack =(Button) findViewById(R.id.back);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        setDataToView(user);

        //add a auth listener
        authListener = new FirebaseAuth.AuthStateListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.d("ActivityTwo", "onAuthStateChanged");
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    setDataToView(user);

                    //loading image by Picasso
                    if (user.getPhotoUrl() != null) {
                        Log.d("ActivityTwo", "photoURL: " + user.getPhotoUrl());
                       // Picasso.with(viewUser.this).load(user.getPhotoUrl()).into(imageView);
                    }
                } else {
                    //user auth state is not existed or closed, return to Login activity
                    startActivity(new Intent(viewUser.this, Login.class));
                    finish();
                }
            }
        };

        //Signing out
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
            }
        });

        //Back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(viewUser.this, myProfile.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setDataToView(FirebaseUser user) {
        email.setText("User Email: " + user.getEmail());
        name.setText("User name: " + user.getDisplayName());
        userId.setText("User id: " + user.getUid());
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
    }




