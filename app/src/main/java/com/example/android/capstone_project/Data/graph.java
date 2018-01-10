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
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

public class graph extends AppCompatActivity {
    PointsGraphSeries<DataPoint> series;
    PointsGraphSeries<DataPoint> onClickSeries;
    int a;
    int b;

    private static final String TAG = "ViewDatabase";

    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mauth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private GraphView mgraph;
    private ArrayList<Integer> xAxis = new ArrayList<>();// Array list for Xaxis
    private ArrayList<Integer> yAxis = new ArrayList<>();// Array list for Yaxis


    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        //Firesbase instance
        mauth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mauth.getCurrentUser();
        //It will get the current user
        userID = user.getUid();
        mgraph = (GraphView) findViewById(R.id.graph);

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
                // ...
            }
        };

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
                    System.out.println("date" + part1);

                    String humidity = sta.substring(40, 45);
                    System.out.println("humidity level" + humidity);

                    try {
                        a = NumberFormat.getInstance().parse(humidity).intValue();
                        xAxis.add(a);//Here data is added to xAxis
                        b = NumberFormat.getInstance().parse(part1).intValue();
                        yAxis.add(b);//Here daat is added to yAxis
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    DataPoint[] values = new DataPoint[xAxis.size()];
                    for (int i = 0; i < xAxis.size(); i++) {

                        DataPoint v = new DataPoint(xAxis.get(i), yAxis.get(i));
                        values[i] = v;
                    }
                    series = new PointsGraphSeries<DataPoint>(values);
                }
                createScatterPlot();

            }


            //This method will get the value of X and Y axis
            private void createScatterPlot() {
                series.setOnDataPointTapListener(new OnDataPointTapListener() {
                    @Override
                    public void onTap(Series series, DataPointInterface dataPointInterface) {

                        Log.d(TAG, "onTap: You clicked on: (" + dataPointInterface.getX() + "," + dataPointInterface.getY() + ")");

                        //declare new series
                        onClickSeries = new PointsGraphSeries<>();
                        onClickSeries.appendData(new DataPoint(dataPointInterface.getX(), dataPointInterface.getY()), true, 100);
                        onClickSeries.setShape(PointsGraphSeries.Shape.TRIANGLE);
                        onClickSeries.setColor(Color.RED);
                        onClickSeries.setSize(25f);


                        double date = dataPointInterface.getY();
                        String date1 = Double.toString(date);
                        String date2 = date1.substring(0, 2);

                        mgraph.removeAllSeries();
                        mgraph.addSeries(onClickSeries);
                        toastMessage("Moisture Level:  " + dataPointInterface.getX() + "\n" + "Date Of Month:  " + date2);
                        createScatterPlot();

                    }
                });

                series.setShape(PointsGraphSeries.Shape.TRIANGLE);//This will set the shape of data to triangle
                series.setColor(Color.GREEN);//This will set the data to green

                // set manual X bounds
                mgraph.getViewport().setYAxisBoundsManual(true);
                mgraph.getViewport().setMinY(0);//It will set the Y axis to minimum value of 0
                mgraph.getViewport().setMaxY(30);//It will set the Y axis to maximum value of 30
                mgraph.getViewport().setXAxisBoundsManual(true);
                mgraph.getViewport().setMinX(0);//It will set the X axis to minimum value of 0
                mgraph.getViewport().setMaxX(100);//It will set the X axis to maximum value of 100
                mgraph.getViewport().setScrollable(true); // enables horizontal scrolling
                mgraph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
                mgraph.addSeries(series);

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
