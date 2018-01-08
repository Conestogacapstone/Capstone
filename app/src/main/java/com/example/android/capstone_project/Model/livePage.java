package com.example.android.capstone_project.Model;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.android.capstone_project.Activities.ActivityTwo;
import com.example.android.capstone_project.R;
import com.example.android.capstone_project.Data.graph;
import com.example.android.capstone_project.Data.history;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import pl.droidsonroids.gif.GifImageView;


public class livePage extends AppCompatActivity {
    private static final String TAG = "ViewDatabase";
    private Button btnBack;
    JSONObject jsonObject = new JSONObject();
    private Button btnSignout, mhistory, btnmoisture, btnwaterOn, btnwaterOff;
    private ToggleButton btntoggle;
    private TextView mtextview;
    private GifImageView mgif;
    private ImageView mImageView;
    private ProgressDialog mProgress;
    CountDownTimer timer;
    Boolean isClickedon, isClickedoff; // global after the declaration of your class




    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private StorageReference mStorageRef;
    DataSnapshot storageRef;

    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_page);


        mStorageRef = FirebaseStorage.getInstance().getReference();
       // btnBack = (Button) findViewById(R.id.backBtn);
        mhistory = (Button) findViewById(R.id.history);
        btnmoisture = (Button) findViewById(R.id.moisture);
        btnwaterOn = (Button) findViewById(R.id.waterOn);
        btnwaterOff = (Button) findViewById(R.id.waterOff);
        mtextview = (TextView) findViewById(R.id.sensorValue);
       // btnSignout = (Button) findViewById(R.id.sign_out);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mgif =(GifImageView) findViewById(R.id.gifgh);

        // Add back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        mProgress = new ProgressDialog(this);
        isClickedon = false;
        isClickedoff = false;


        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();



        //Authentication method
        //NOTE: Unless you are signed in, this will not be useable.
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

        //This function is setting and getting random value from database
        btnmoisture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("x-aio-key", "47f1d473707041829ad00da61f99da23");
                client.get("https://io.adafruit.com/api/v2/capsProject/feeds/sensor/data", params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                        // Handle resulting parsed JSON response here
                        try {
                            JSONObject firstArray = response.getJSONObject(0);
                            String responseData = firstArray.getString("value").toString();
                            float data = Float.parseFloat(responseData);
                            mtextview.setText(responseData);

                            if (data <= 25) {
                                mImageView.setImageResource(R.drawable.dead);

                            } else if (data <= 50) {
                                mImageView.setImageResource(R.drawable.unhealthy);

                            } else if (data <= 75) {
                                mImageView.setImageResource(R.drawable.mhealthy);

                            } else if (data <= 100) {
                                mImageView.setImageResource(R.drawable.vhealthy);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    }
                });



            }


        });



            btnwaterOn.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {

                    int clicks = 0;
                    clicks++;

                    if (clicks == 1){
                        btnwaterOn.setEnabled(false);
                    }

                    mgif.setVisibility(View.VISIBLE);



                    btnwaterOff.setBackgroundColor(Color.parseColor("#D4E157"));
                    btnwaterOn.setBackgroundColor(Color.parseColor("#00b200"));


                    // Date currentTime = Calendar.getInstance().getTime();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = new Date();
                    //  System.out.println(formatter.format(date));
                    SimpleDateFormat formatter1 = new SimpleDateFormat(":HH:mm:ss");
                    Date date1 = new Date();
                    //System.out.println(formatter1.format(date1));
                    //   final String historyObject = currentTime.toString();
                    final String obj1 = formatter.format(date);
                    final String obj2 = formatter1.format(date1);


                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.put("x-aio-key", "47f1d473707041829ad00da61f99da23");
                    client.get("https://io.adafruit.com/api/v2/capsProject/feeds/sensor/data", params, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                            // Handle resulting parsed JSON response here
                            try {
                                JSONObject firstArray = response.getJSONObject(0);
                                String responseData = firstArray.getString("value").toString();
                                //Here date and time are pushed to database
                                myRef.child("users").child(userID).child("History").push().setValue(obj1 + "Time" + obj2 + " " + "Moisture level-" + " " + responseData+"       ");


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        }
                    });




                    try {


                        //  AsyncHttpClient client = new AsyncHttpClient();
                        StringEntity entity;
                        // JSONObject jsonObject = new JSONObject();
                        jsonObject.put("value", "ON");
                        entity = new StringEntity(jsonObject.toString());


                        client.post(livePage.this, "https://io.adafruit.com/api/v2/capsProject/feeds/valve/data?x-aio-key=47f1d473707041829ad00da61f99da23", entity, "application/json", new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                System.out.println("Sucess");
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            }
                        });

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        System.out.println("Error " + e);
                    }




                    timer = new CountDownTimer(10000, 1000) {


                        AsyncHttpClient client = new AsyncHttpClient();

                        public void onTick(long millisUntilFinished) {

                            System.out.println("seconds remaining: " + millisUntilFinished / 1000);





                        }

                        public void onFinish() {
                            System.out.println("done");
                            btnwaterOn.setEnabled(true);


                            try {
                                StringEntity entity;
                                // JSONObject jsonObject = new JSONObject();
                                jsonObject.put("value", "OFF");
                                entity = new StringEntity(jsonObject.toString());


                                client.post(livePage.this, "https://io.adafruit.com/api/v2/capsProject/feeds/valve/data?x-aio-key=47f1d473707041829ad00da61f99da23", entity, "application/json", new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        System.out.println("Sucess");
                                        btnwaterOff.setBackgroundColor(Color.parseColor("#FF0000"));
                                        btnwaterOn.setBackgroundColor(Color.parseColor("#D4E157"));
                                        mgif.setVisibility(View.INVISIBLE);
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                    }
                                });

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                    };
                    timer.start();

                }
            });




        //This is onClickListner to tap Off the water pump
        btnwaterOff.setOnClickListener(new View.OnClickListener() {






            AsyncHttpClient client = new AsyncHttpClient();

            @Override
            public void onClick(View v) {
                if (timer != null) {
                    timer.cancel();



                }
                btnwaterOn.setEnabled(true);
                mgif.setVisibility(View.INVISIBLE);

                btnwaterOff.setBackgroundColor(Color.parseColor("#FF0000"));
                btnwaterOn.setBackgroundColor(Color.parseColor("#D4E157"));

                try {
                    StringEntity entity;
                    // JSONObject jsonObject = new JSONObject();
                    jsonObject.put("value", "OFF");
                    entity = new StringEntity(jsonObject.toString());


                    client.post(livePage.this, "https://io.adafruit.com/api/v2/capsProject/feeds/valve/data?x-aio-key=47f1d473707041829ad00da61f99da23", entity, "application/json", new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            System.out.println("Sucess");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });


//        //This is onClickListner to return to homepage of application
//        btnBack.setOnClickListener(new View.OnClickListener() {
//
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(livePage.this, ActivityTwo.class);
//                startActivity(intent);
//
//
//
//
//
//            }
//        });

//        //This is onClickListner to Signout from the application
//        btnSignout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mAuth.signOut();
//                toastMessage("Signing Out...");
//                Intent intent = new Intent(livePage.this, Login.class);
//                startActivity(intent);
//            }
//        });


    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.live_menu, menu);
        return true;
    }


    //These are the different menu options
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.listView:
                Intent intent1 = new Intent(livePage.this, history.class);
                startActivity(intent1);
                return true;


            case R.id.graphView:
                Intent intent2 = new Intent(livePage.this, graph.class);
                startActivity(intent2);
                return true;

            case R.id.reminder:
                Intent intent3 = new Intent(livePage.this, ActivityTwo.class);
                startActivity(intent3);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
