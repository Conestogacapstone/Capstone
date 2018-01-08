package com.example.android.capstone_project.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.capstone_project.R;

public class plant13 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant13);

        // Add back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
