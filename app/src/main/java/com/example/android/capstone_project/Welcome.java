package com.example.android.capstone_project;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import java.util.Timer;


public class Welcome extends AppCompatActivity {
    protected boolean _active = true;
    protected int _splashTime = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                finish();
                Intent intent = new Intent(Welcome.this, livePage.class);
                startActivity(intent);
            }
        }, _splashTime);



    }




}
