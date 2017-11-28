package com.example.android.capstone_project;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

public class Myplants extends AppCompatActivity {
    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
    android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myplants);

        String [] menuItems ={"A","B","C"};

        ListView listView = (ListView) findViewById(R.id.mainMenu);

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                Myplants.this,
                android.R.layout.simple_list_item_1,
                menuItems
        );

        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0){
                    android.support.v4.app.FragmentManager fragmentManager = Myplants.this.getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    plant_1 fragment = new plant_1();
                    fragmentTransaction.replace(R.id.content,new plant_1()).commit();


                }
                else if(position == 1)
                {
                    Toast.makeText(Myplants.this,"yes",Toast.LENGTH_SHORT).show();


                }


            }
        });
    }
}
