package com.example.android.capstone_project.Data;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.capstone_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class resetPassword extends AppCompatActivity {

    private EditText Email;
    private Button Reset_button, Back_button;
    private FirebaseAuth mauth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //This is the view
        setContentView(R.layout.activity_reset_password);

        Email = (EditText) findViewById(R.id.inputemail); // Getting user's email to Email
        Reset_button = (Button) findViewById(R.id.resetpassword); // Getting reset button
        Back_button = (Button) findViewById(R.id.back); // Getting back button
        progressBar = (ProgressBar) findViewById(R.id.progressBar); // Getting progressBar

        //Instance of Firebase
        mauth = FirebaseAuth.getInstance();

        // This method will reset the users password
        Reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = Email.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Please enter your Email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Visibility of progress bar is set to visible
                progressBar.setVisibility(View.VISIBLE);

                // This method will send reset password email to users email address
                mauth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(resetPassword.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(resetPassword.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                }

                                //Visibility of progress bar is set to invisible
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });


        //This is the back button which will take user to login page.
        Back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

}

