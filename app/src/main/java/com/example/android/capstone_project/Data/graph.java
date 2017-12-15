package com.example.android.capstone_project.Data;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

public class graph extends AppCompatActivity {
    PointsGraphSeries<DataPoint> series;
    int a;
    int b;

    private static final String TAG = "ViewDatabase";

    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private ListView mlistview;
    private Button mdel;
    private GraphView mgraph;
    private ArrayList<String> mHistory = new ArrayList<>();
    private ArrayList<Integer> xAxis = new ArrayList<>();
    private ArrayList<Integer> yAxis = new ArrayList<>();



    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        mgraph = (GraphView) findViewById(R.id.graph);


//
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Successfully signed out.");
                }
                // ...
            }
        };


        // System.out.println(historyObject);
        myRef.child("users").child(userID).child("History").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                // Iterable used for "For loop" for any data type
                Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                //For loop to get all data
                for (DataSnapshot ds : dataSnapshots) {
                    //getting value to a string 'sta'
                    String sta = ds.getValue().toString();

                    String[] parts = sta.split("/");
                    String part1 = parts[0];
                    System.out.println("date"+part1);

                    String [] part = sta.split(":");
                    String part2 = part[1];
                    System.out.println("time"+part2);


                    try {


                        a = NumberFormat.getInstance().parse(part1).intValue();
                        //System.out.println("log value" + a);
                        xAxis.add(a);
                      //  System.out.println("Arraylist" + xAxis);


                        b = NumberFormat.getInstance().parse(part2).intValue();
                        yAxis.add(b);


                        //System.out.println(i);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    DataPoint[] values = new DataPoint[xAxis.size()];
                    for (int i = 0; i < xAxis.size(); i++) {

                        DataPoint v = new DataPoint(xAxis.get(i), yAxis.get(i));
                        values[i] = v;
                    }
                   // System.out.println("Data values-----" + values);
                    series = new PointsGraphSeries<DataPoint>(values);
                    series.setColor(Color.GREEN);
                    GridLabelRenderer gridLabel = mgraph.getGridLabelRenderer();
                    gridLabel.setHorizontalAxisTitle("Date");
                    gridLabel.setVerticalAxisTitle("Time in Hours");
//
//                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
//                            new DataPoint(xAxis.get(0),yAxis.get(0))
//                    });
                    mgraph.addSeries(series);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

//        myRef.child()
//        mdel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myRef.child("users").child(userID).child("History").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        // Iterable used for "For loop" for any data type
//                       // Iterable<DataSnapshot> dataSnapshots =  dataSnapshot.getChildren();
//                        //For loop to get all data
//                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
//
//
//                            ds.getRef().removeValue();
//                          //  adapter.notifyDataSetChanged();
//
//
//                        }
//
//                    }
//
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        // Getting Post failed, log a message
//                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
//                        // ...
//                    }
//                });
//
//            }
//        });


    }


// Read from the database


    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
