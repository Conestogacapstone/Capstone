package com.example.android.capstone_project.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.android.capstone_project.Model.livePage;
import com.example.android.capstone_project.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "ActivityTwo";

    private Button btnlive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //hide action bar
        getSupportActionBar().hide();

        btnlive = (Button) findViewById(R.id.livestreamBtn);


        //Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.home) {
                } else if (i == R.id.myPlants) {


                    Intent intent2 = new Intent(MainActivity.this, ActivityOne.class);
                    startActivity(intent2);


                } else if (i == R.id.settings) {
                    Intent intent3 = new Intent(MainActivity.this, ActivityThree.class);
                    startActivity(intent3);

                }


                return false;
            }
        });

        //Start Button to move to livepage
        btnlive.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, livePage.class);
                startActivity(intent);
            }
        });

    }


}
