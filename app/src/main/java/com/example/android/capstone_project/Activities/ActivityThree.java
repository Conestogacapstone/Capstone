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
import android.widget.Toolbar;

import com.example.android.capstone_project.Data.myProfile;
import com.example.android.capstone_project.R;


public class ActivityThree extends AppCompatActivity {
    Toolbar toolbar;
    ListView listview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);


        //Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.home) {
                    Intent intent0 = new Intent(ActivityThree.this, MainActivity.class);
                    startActivity(intent0);

                } else if (i == R.id.myPlants) {
                    Intent intent1 = new Intent(ActivityThree.this, ActivityOne.class);
                    startActivity(intent1);

                } else if (i == R.id.settings) {
                }


                return false;
            }
        });
        //String array to store menu items
        String[] menuItems = {"Edit Profile", "About"};


        //List view created
        final ListView listView = (ListView) findViewById(R.id.mainMenu);


        //Array adapter to hold listview
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                ActivityThree.this,
                android.R.layout.simple_list_item_1,
                menuItems

        );

        listView.setAdapter(listViewAdapter);


        //List view conditions
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    Intent intent0 = new Intent(ActivityThree.this, myProfile.class);
                    startActivity(intent0);


                } else if (position == 1) {
                    Intent intent2 = new Intent(ActivityThree.this, About.class);
                    startActivity(intent2);


                }


            }
        });

    }

}
