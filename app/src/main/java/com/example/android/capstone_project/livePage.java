package com.example.android.capstone_project;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static android.R.attr.contextClickable;
import static android.R.attr.data;
import static android.R.attr.fragment;
import static android.R.attr.name;
import static android.R.attr.thumbnail;


public class livePage extends AppCompatActivity {
    private static final String TAG = "ViewDatabase";
    private Button btnBack;

    private Button mUploadBtn,btnSignout,mDownload,mLesswater;
    private ImageView mImageView;

    private ProgressDialog mProgress;

    private static final int CAMERA_REQUEST_CODE = 1;

    //  private StorageReference mStorage;

    Uri imageUri;


    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private StorageReference mStorageRef;
    DataSnapshot storageRef;

    String userID;

    //  private ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        setContentView(R.layout.activity_live_page);
        btnBack = (Button) findViewById(R.id.backBtn);
        mDownload = (Button) findViewById(R.id.download);
        mLesswater = (Button) findViewById(R.id.lesswater);
        //  mListView = (ListView) findViewById(R.id.show_data);

        // mStorageRef = FirebaseStorage.getInstance().getReference();

        // mStorage = FirebaseStorage.getInstance().getReference();




        mUploadBtn = (Button) findViewById(R.id.upload);
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


        mUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);


            }
        });
        mUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);


            }
        });

        mLesswater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




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
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle extras = data.getExtras();
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        final byte[] dataBAOS = baos.toByteArray();


        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            mProgress.setMessage("Uploading Image..");
            mProgress.show();

            mDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle extras = data.getExtras();
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    final byte[] dataBAOS = baos.toByteArray();

                    //set the image into imageview
                    mImageView.setImageBitmap(bitmap);

                }
            });





                /*************** UPLOADS THE PIC TO FIREBASE***************/
                //Firebase storage folder where you want to put the images
                StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://capstoneproject-e8b4d.appspot.com");

//name of the image file (add time to have different files to avoid rewrite on the same file)

                StorageReference imagesRef = storageRef.child("file" + userID + "/" + name + ".jpg" );



                toastMessage("New Information has been saved.");

/*

            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            final byte[] dataBAOS = baos.toByteArray();*/





            //upload image

            UploadTask uploadTask = imagesRef.putBytes(dataBAOS);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    mProgress.dismiss();

                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Picasso.with(livePage.this).load(downloadUrl).fit().centerCrop();


                    Toast.makeText(livePage.this, "Uploading done!!!!", Toast.LENGTH_LONG).show();

                }
            });

        }

    }








        /*
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
                }


                else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Successfully signed out.");
                }
                // ...
            }
        };


        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    private void showData(DataSnapshot dataSnapshot){
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            UserInformation uInfo = new UserInformation();
            uInfo.setName(ds.child(userID).getValue(UserInformation.class).getName()); //set the name
            uInfo.setEmail(ds.child(userID).getValue(UserInformation.class).getEmail()); //set the email
            uInfo.setPhone_num(ds.child(userID).getValue(UserInformation.class).getPhone_num()); //set the phone_num

            //display all the information
            Log.d(TAG, "showData: name: " + uInfo.getName());
            Log.d(TAG, "showData: email: " + uInfo.getEmail());
            Log.d(TAG, "showData: phone_num: " + uInfo.getPhone_num());

            ArrayList<String> array  = new ArrayList<>();
            array.add(uInfo.getName());
            array.add(uInfo.getEmail());
            array.add(uInfo.getPhone_num());
            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,array);
           mListView.setAdapter(adapter);

        }
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

*/

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
}
