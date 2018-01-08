package com.example.android.capstone_project.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android.capstone_project.R;


public class ActivityOne extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);




        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.home) {
                    Intent intent0 = new Intent(ActivityOne.this, MainActivity.class);
                    startActivity(intent0);

                } else if (i == R.id.myPlants) {
                } else if (i == R.id.settings) {
                    Intent intent3 = new Intent(ActivityOne.this, ActivityThree.class);
                    startActivity(intent3);

                }


                return false;
            }
        });

        String[] menuItems = {"Adromischus cooperi", "Aeonium", "Aloe Vera","Avocado","Basil","Black orchid","Bush lily","Cacao","Cardamom","Cattleya-orchid","Lavender lady",
                "Mophead","Petunia calibrachoa","Rose","Viola",""};

        ListView listView = (ListView) findViewById(R.id.mainMenu);

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                ActivityOne.this,
                android.R.layout.simple_list_item_1,
                menuItems
        );

        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    Intent intent2 = new Intent(ActivityOne.this, plant1.class);
                    startActivity(intent2);


                } else if (position == 1) {
                    Intent intent2 = new Intent(ActivityOne.this, plant2.class);
                    startActivity(intent2);

                }
                else if (position == 2) {
                    Intent intent2 = new Intent(ActivityOne.this, plant3.class);
                    startActivity(intent2);

                }
                else if (position == 3) {
                    Intent intent2 = new Intent(ActivityOne.this, plant4.class);
                    startActivity(intent2);

                }
                else if (position == 4) {
                    Intent intent2 = new Intent(ActivityOne.this, plant5.class);
                    startActivity(intent2);

                }
                else if (position == 5) {
                    Intent intent2 = new Intent(ActivityOne.this, plant6.class);
                    startActivity(intent2);

                }
                else if (position == 6) {
                    Intent intent2 = new Intent(ActivityOne.this, plant7.class);
                    startActivity(intent2);

                }
                else if (position == 7) {
                    Intent intent2 = new Intent(ActivityOne.this, plant8.class);
                    startActivity(intent2);

                }
                else if (position == 8) {
                    Intent intent2 = new Intent(ActivityOne.this, plant9.class);
                    startActivity(intent2);

                }
                else if (position == 9) {
                    Intent intent2 = new Intent(ActivityOne.this, plant10.class);
                    startActivity(intent2);

                }
                else if (position == 10) {
                    Intent intent2 = new Intent(ActivityOne.this, plant11.class);
                    startActivity(intent2);

                }
                else if (position == 11) {
                    Intent intent2 = new Intent(ActivityOne.this, plant12.class);
                    startActivity(intent2);

                }
                else if (position == 12) {
                    Intent intent2 = new Intent(ActivityOne.this, plant13.class);
                    startActivity(intent2);

                }
                else if (position == 13) {
                    Intent intent2 = new Intent(ActivityOne.this, plant14.class);
                    startActivity(intent2);

                }
                else if (position == 14) {
                    Intent intent2 = new Intent(ActivityOne.this, plant15.class);
                    startActivity(intent2);

                }


            }
        });
    }

}
