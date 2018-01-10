package com.example.android.capstone_project.Data;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.capstone_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Ref;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class history extends AppCompatActivity {
    private static final String TAG = "ViewDatabase";

    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mauth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private ListView mlistview;
    private Button mdel;
    private ArrayList<String> mHistory = new ArrayList<>();//This is the array list to add history

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //This is the view
        setContentView(R.layout.activity_history);

        //This is the instance of Firebase
        mauth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mauth.getCurrentUser();//This will get the current user from the database
        userID = user.getUid();

        mlistview = (ListView) findViewById(R.id.historyList);//Getting list here
        mdel = (Button) findViewById(R.id.del);//Getting delete button here

        // Add back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    toastMessage("Successfully signed out.");
                }

            }
        };


        //This is the list adapter used for array
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mHistory);
        mlistview.setAdapter(adapter);
        myRef.child("users").child(userID).child("History").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                // Iterable used for "For loop" for any data type
                Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                //For loop to get all data
                for (DataSnapshot ds : dataSnapshots) {
                    //getting value to a string 'sta'
                    String sta = ds.getValue().toString();
                    String date = sta.substring(0, 10);
                    String del = sta.substring(0, 2);
                    int del1 = Integer.parseInt(del);
                    String time = sta.substring(10, 20);
                    String humidity = sta.substring(23, 45);


                    //Here date, humidity and time are added to list view
                    mHistory.add("Date-" + date + "             " + humidity + "\n" + time + "\n");
                    adapter.notifyDataSetChanged();
                    mlistview.setAdapter(adapter);


                    //This condition will delete whole data on every first day of month
                    if (del1 == 01) {
                        dataSnapshot.getRef().removeValue();
                        finish();
                        startActivity(getIntent());
                    }
                }

                //This method will delete data from database
                mdel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dataSnapshot.getRef().removeValue();
                        finish();
                        startActivity(getIntent());
                    }
                });
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

    }


    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
