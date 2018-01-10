package com.example.android.capstone_project.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.capstone_project.Authenticator.Login;
import com.example.android.capstone_project.R;


public class Welcome extends AppCompatActivity {
    protected boolean _active = true;
    protected int _splashTime = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Hiding Action bar
        getSupportActionBar().hide();

        // Handler to move from one page to another
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                finish();
                Intent intent = new Intent(Welcome.this, Login.class);
                startActivity(intent);
            }
        }, _splashTime);


    }


}
