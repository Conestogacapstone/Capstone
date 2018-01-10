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
    JSONObject jsonObject = new JSONObject();
    private Button btnmoisture, btnwaterOn;
    private TextView mtextview;
    private GifImageView mgif;
    private ImageView mImageView,mindicator1,mindicator2,mOn,mOff;
    CountDownTimer timer;
    Boolean isClickedon; // global after the declaration of your class


    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mauth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private StorageReference mStorageRef;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //This is the view
        setContentView(R.layout.activity_live_page);

        mStorageRef = FirebaseStorage.getInstance().getReference();//Getting storage refernce
        btnmoisture = (Button) findViewById(R.id.moisture);//Getting moisture button
        btnwaterOn = (Button) findViewById(R.id.waterOn);//Getting waterOn button
        mtextview = (TextView) findViewById(R.id.sensorValue);//Getting sensor text view
        mImageView = (ImageView) findViewById(R.id.imageView);//Getting imageview to set the image according to sensor values
        mgif = (GifImageView) findViewById(R.id.gifgh);//Getting Gif view
        mindicator1 =(ImageView) findViewById(R.id.green);//Getting indicator
        mindicator2 =(ImageView) findViewById(R.id.red);//getting indicator
        mOff =(ImageView) findViewById(R.id.off);//getting indicator
        mOn =(ImageView) findViewById(R.id.on);//getting indicator


        // Add back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        isClickedon = false;//Boolean variable for waterOn pump
       // isClickedoff = false;//Boolean value for waterOff pump

        //Instance of firebase
        mauth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mauth.getCurrentUser();//Getting current user
        userID = user.getUid();//Getting unique Id of the user

        //Authentication method
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

        //This function is getting moisture level from database
        btnmoisture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Asynch Http request to get data from cloud
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();//requesting parameters from cloud
                params.put("x-aio-key", "f1b3f9d0456d404a8d68e86ee3661b21");//Passing Adafruit key here
                //Get request to get Data from cloud
                client.get("https://io.adafruit.com/api/v2/capsProject/feeds/sensor/data", params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        // Handle resulting parsed JSON response here
                        try {
                            JSONObject firstArray = response.getJSONObject(0);
                            String responseData = firstArray.getString("value").toString();//Storing data in a String variable
                            float data = Float.parseFloat(responseData);
                            mtextview.setText(responseData);//Setting the data in a textfield

                            //Conditions to set the image according to the sensor value
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

        //This method is to set water pump On
        btnwaterOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Restrict the number of clicks on water pump
                int clicks = 0;
                clicks++;

                if (clicks == 1) {
                    btnwaterOn.setEnabled(false);
                }
                mgif.setVisibility(View.VISIBLE);//Setting Gif when user clicks WaterOn button
                mindicator1.setVisibility(View.VISIBLE);//Setting green indicator visible
                mindicator2.setVisibility(View.INVISIBLE);//Setting indicator invisible
                mOn.setVisibility(View.VISIBLE);//Setting indicator On
                mOff.setVisibility(View.INVISIBLE);//Setting indicator off

                //Changing color of button as the user clicks the button
                btnwaterOn.setBackgroundColor(Color.parseColor("#00b200"));

                //These are the date formatters to get the date at the time user hits wateron button and storing data in firebase
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                SimpleDateFormat formatter1 = new SimpleDateFormat(":HH:mm:ss");
                Date date1 = new Date();
                final String obj1 = formatter.format(date);
                final String obj2 = formatter1.format(date1);


                //This is the Asynch Http request
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("x-aio-key", "f1b3f9d0456d404a8d68e86ee3661b21");
                client.get("https://io.adafruit.com/api/v2/capsProject/feeds/sensor/data", params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        // Handle resulting parsed JSON response here
                        try {
                            JSONObject firstArray = response.getJSONObject(0);
                            String responseData = firstArray.getString("value").toString();//Storing data in a String variable
                            //Here date and time are pushed to database
                            myRef.child("users").child(userID).child("History").push().setValue(obj1 + "Time" + obj2 + " " + "Moisture level-" + " " + responseData + "       ");


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

                    StringEntity entity;
                    jsonObject.put("value", "on");
                    entity = new StringEntity(jsonObject.toString());
                    //This is the post method to set the toggle On and off in the adafruit
                    client.post(livePage.this, "https://io.adafruit.com/api/v2/capsProject/feeds/valve/data?x-aio-key=f1b3f9d0456d404a8d68e86ee3661b21", entity, "application/json", new AsyncHttpResponseHandler() {
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

                //This is the timer to set the timer for sometime
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
                            jsonObject.put("value", "OFF");
                            entity = new StringEntity(jsonObject.toString());
                            //This is the method to post the data in the adafruit cloud
                            client.post(livePage.this, "https://io.adafruit.com/api/v2/capsProject/feeds/valve/data?x-aio-key=f1b3f9d0456d404a8d68e86ee3661b21", entity, "application/json", new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    System.out.println("Sucess");
                                    btnwaterOn.setBackgroundColor(Color.parseColor("#FF0000"));
                                    mgif.setVisibility(View.INVISIBLE);
                                    mindicator1.setVisibility(View.INVISIBLE);//Setting indicator invisible
                                    mindicator2.setVisibility(View.VISIBLE);//Setting indicator visible
                                    mOn.setVisibility(View.INVISIBLE);//Setting indicator off
                                    mOff.setVisibility(View.VISIBLE);//Setting indicator On
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
                //Here the timer has been set to start stage
                timer.start();
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        mauth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mauth.removeAuthStateListener(mAuthListener);
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
