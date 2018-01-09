package com.example.android.capstone_project.Authenticator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.example.android.capstone_project.R;
import com.example.android.capstone_project.Data.resetPassword;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


public class signUp extends AppCompatActivity {


    private EditText Email, Password, Repassword;
    private Button SignIn_button, SignUp_button, ResetPassword_button;
    private ProgressBar progressBar;
    private FirebaseAuth mauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This is the view
        setContentView(R.layout.activity_sign_up);


        //Get Firebase auth instance
        mauth = FirebaseAuth.getInstance();


        Email = (EditText) findViewById(R.id.inputemail); // Getting user's email to Email
        Password = (EditText) findViewById(R.id.inputpassword); // Getting user's password to Password
        Repassword = (EditText) findViewById(R.id.inputrepassword); // Getting user's Confirmational password to Repassword
        progressBar = (ProgressBar) findViewById(R.id.progressBar); // Getting progressBar
        ResetPassword_button = (Button) findViewById(R.id.reset_password); // Getting reset button
        SignIn_button = (Button) findViewById(R.id.signin); // Getting Sign_in button
        SignUp_button = (Button) findViewById(R.id.signup); // Getting Sign_up button


        //Password reset button
        ResetPassword_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signUp.this, resetPassword.class));
            }
        });

        //Sign in button to move to Signin page
        SignIn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signUp.this, Login.class);
                startActivity(intent);

            }
        });


        // This is the Signup button
        SignUp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();
                String repassword = Repassword.getText().toString().trim();


                // If Email field is empty.
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Please Enter your email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // If Password field is empty.
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Please Enter your password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // If Re-Password field is empty.
                if (TextUtils.isEmpty(repassword)) {
                    Toast.makeText(getApplicationContext(), "Please Re-Enter your password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // If Passwords do not match.
                if (!password.equals(repassword)) {
                    Toast.makeText(getApplicationContext(), "Password not matched!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // If Password's length is short then required.
                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Please Enter minimum of 6 characters in your password field!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Visibility of progress bar is set to visible
                progressBar.setVisibility(View.VISIBLE);

                // A new user will be created with this method
                mauth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(signUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(signUp.this, "User created with email:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // This method will display a message to the user if Signup fails.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(signUp.this, "Oops something went wrong, Failed to Register" + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                }
                                //If Log in is successful then it will redirect user to Login page.
                                else {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    user.sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(signUp.this, "Registration was successful and the verification mail was sent to your E-mail", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                    Intent intent = new Intent(signUp.this, Login.class);
                                    startActivity(intent);
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}

