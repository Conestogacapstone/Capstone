package com.example.android.capstone_project;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Button btnlive;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnlive = (Button) findViewById(R.id.livestreamBtn);





        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.home) {
                } else if (i == R.id.myPlants) {


                    Intent intent2 = new Intent(MainActivity.this, ActivityOne.class);
                    startActivity(intent2);


                } else if (i == R.id.reminder) {
                    Intent intent2 = new Intent(MainActivity.this, ActivityTwo.class);
                    startActivity(intent2);

                } else if (i == R.id.settings) {
                    Intent intent3 = new Intent(MainActivity.this, ActivityThree.class);
                    startActivity(intent3);

                }


                return false;
            }
        });

        btnlive.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, livePage.class);
                startActivity(intent);
            }
        });

    }



}
