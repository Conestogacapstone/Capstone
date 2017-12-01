package com.example.android.capstone_project;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;



public class ActivityOne extends AppCompatActivity  {

    Toolbar toolbar;
    ListView listview;

    FragmentManager fragmentManager = getSupportFragmentManager();
    android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();



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
                } /*else if (i == R.id.reminder) {
                    Intent intent2 = new Intent(ActivityOne.this, ActivityTwo.class);
                    startActivity(intent2);

                }*/ else if (i == R.id.settings) {
                    Intent intent3 = new Intent(ActivityOne.this, ActivityThree.class);
                    startActivity(intent3);

                }



                return false;
            }
        });

        String [] menuItems ={"A","B","C"};

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

                if(position == 0){
                    Intent intent2 = new Intent(ActivityOne.this, plant1.class);
                    startActivity(intent2);


                }
                else if(position == 1)
                {
                    Toast.makeText(ActivityOne.this,"yes",Toast.LENGTH_SHORT).show();


                }


            }
        });
    }
}
