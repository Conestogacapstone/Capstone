package com.example.android.capstone_project;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import android.widget.Toast;
import android.widget.Toolbar;

import java.net.SocketPermission;
import java.sql.SQLOutput;
import java.util.Calendar;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import static android.R.attr.contextClickable;
import static android.R.attr.data;
import static android.R.attr.defaultHeight;
import static android.R.attr.fragment;
import static android.R.attr.logo;
import static android.R.attr.name;
import static android.R.attr.noHistory;
import static android.R.attr.thumbnail;


public class livePage extends AppCompatActivity {
    private static final String TAG = "ViewDatabase";
    private Button btnBack;

    private Button btnSignout, mhistory, btnmoisture,btnwaterOn,btnwaterOff;
    private TextView mtextview;
    private ImageView mImageView;
    private final int PREFERENCES_ACTIVITY = 2;

    private ProgressDialog mProgress;

    private static final int CAMERA_REQUEST_CODE = 1;

    // private StorageReference mStorage;

    Uri imageUri;


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
        btnBack = (Button) findViewById(R.id.backBtn);
        // mDownload = (Button) findViewById(R.id.download);
        // mLesswater = (Button) findViewById(R.id.lesswater);
        mhistory = (Button) findViewById(R.id.history);
        btnmoisture = (Button) findViewById(R.id.moisture);
        btnwaterOn = (Button) findViewById(R.id.waterOn);
        btnwaterOff = (Button) findViewById(R.id.waterOff);
        mtextview = (TextView) findViewById(R.id.rand);
        // mListView = (ListView) findViewById(R.id.show_data);

        // mStorageRef = FirebaseStorage.getInstance().getReference();

        // mStorage = FirebaseStorage.getInstance().getReference();


        // mUploadBtn = (Button) findViewById(R.id.upload);
        btnSignout = (Button) findViewById(R.id.sign_out);
        mImageView = (ImageView) findViewById(R.id.imageView);

        mProgress = new ProgressDialog(this);
        //declare the database reference object. This is what we use to access the database.
        //NOTE: Unless you are signed in, this will not be useable.
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();


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


        mhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Date currentTime = Calendar.getInstance().getTime();

                     String historyObject = currentTime.toString();


                    myRef.child("users").child(userID).child("History").push().setValue(historyObject);

                } catch (Exception e) {
                    System.out.println("Error " + e);
                }
            }
        });

        //This function is setting and getting random value from database
        btnmoisture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random r = new Random();
                int randomNumber = r.nextInt(100 - 1) + 0;

                myRef.child("users").child(userID).child("RandomNumber").setValue(randomNumber);
                int ran = randomNumber;
                String random = Integer.toString(ran);
                mtextview.setText(random);

                if(ran <= 25)
                {
                    mImageView.setImageResource(R.drawable.dead);

                }
               else if(ran <= 50)
                {
                    mImageView.setImageResource(R.drawable.unhealthy);

                }
                else if(ran <= 75)
                {
                    mImageView.setImageResource(R.drawable.mhealthy);

                }
                else if(ran <= 100)
                {
                    mImageView.setImageResource(R.drawable.vhealthy);

                }
            }



        });

        btnwaterOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              int waterOn = 1;
                myRef.child("users").child(userID).child("status").setValue(waterOn);



            }
        });
        btnwaterOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int waterOff = 0;
                myRef.child("users").child(userID).child("status").setValue(waterOff);



            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent intent = new Intent(livePage.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                toastMessage("Signing Out...");
                Intent intent = new Intent(livePage.this, Login.class);
                startActivity(intent);
            }
        });


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
    public boolean onCreateOptionsMenu(Menu menu)
   {
       getMenuInflater().inflate(R.menu.live_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId()){
            case R.id.h:
            return true;
            case R.id.m:
                return true;
            case R.id.s:
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }


}
