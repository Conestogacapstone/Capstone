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
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class history extends AppCompatActivity {
    private static final String TAG = "ViewDatabase";

    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
   private ListView mlistview;
   private Button mdel;
   private ArrayList<String> mHistory = new ArrayList<>();

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

         mlistview = (ListView) findViewById(R.id.historyList);
        mdel = (Button) findViewById(R.id.del);


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


        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,mHistory);
        mlistview.setAdapter(adapter);
        // System.out.println(historyObject);
        myRef.child("users").child(userID).child("History").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                // Iterable used for "For loop" for any data type
                Iterable<DataSnapshot> dataSnapshots =  dataSnapshot.getChildren();
                //For loop to get all data
                for (DataSnapshot ds : dataSnapshots) {
                    //getting value to a string 'sta'
                    String sta = ds.getValue().toString();
                  //  System.out.println(sta);

                    String [] parts = sta.split("-");
                    String part1=parts[0];
//                    try {
//                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//                    try {
//                        Date date = sdf.parse(sta);
//                        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
//                        calendar.setTime(date);   // assigns calendar to given date
//                        int hour = calendar.get(Calendar.HOUR);
//                        int minute;
//                        System.out.println(hour);
//
//
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    }
//                    catch (Exception e)
//                    {
//                        System.out.println(e);
//
//                    }

                    //Array list used
                  //  ArrayList<String> array  = new ArrayList<>();
                    mHistory.add("Date-"+sta);
                   // mHistory.add(sta1);
                   // System.out.println(array);
                    adapter.notifyDataSetChanged();

                    mlistview.setAdapter(adapter);




                }


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


//
//       // myRef.child()
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
//                            adapter.notifyDataSetChanged();
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
