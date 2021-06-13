package com.hackslash.haaziri.onboarding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hackslash.haaziri.Profile.ProfileActivity;
import com.hackslash.haaziri.R;

public class SignUpActivity extends AppCompatActivity {

    private Context mContext = this;

    EditText nameEdit;
    String email;
    String password;
    EditText emailEdit;
    EditText mobileEdit;
    EditText passwordEdit;
    EditText confirmPassEdit;
    ProgressBar signUpProgress;
    Button saveBtn;
    ImageView backBtn;
    private FirebaseAuth mAuth;
    private static final String TAG = "AnonymousAuth";
    private boolean len=false,hasUppercase=false,hasNumber=false,hasSymbol=false,isRegistrationClickable=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //disabling dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //hiding the action bar
        getSupportActionBar().hide();

        //making status bar white with black icons
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //function to initialize all the layout variables, should be present in all activities
        initVars();



        mAuth = FirebaseAuth.getInstance();



        //function to setup listeners, should be present in all activities
        setupListeners();
    }

    private void setupListeners() {
        saveBtn.setOnClickListener(v -> {
            //if(email and password pattern matches) ->signUpUser
            //else-> show error toast
            email=  emailEdit.getText().toString();
            password=passwordEdit.getText().toString();
            if (password.length() > 6) {
                len = true;
            }
            if (password.matches("(.*[A-Z].*)")) {
                hasUppercase = true;
            }
            if (password.matches("(.*[0-9].*)")) {
                hasNumber = true;
            }
            if (password.matches("^(?=.*[_.()]).*$")) {
                hasSymbol = true;
            }
            if (len && hasSymbol && hasNumber && hasUppercase && email.length() > 0) {

                signupUser();
            }
            else{
                Toast.makeText(mContext, "Invalid input in password or E-mail", Toast.LENGTH_SHORT).show();
            }

        });
        backBtn.setOnClickListener(v -> finish());
    }

    private void signupUser() {
        mAuth.createUserWithEmailAndPassword(email, password)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull  Task<Void> task) {
                                    Toast.makeText(SignUpActivity.this, "Please Veriy ur E-mail", Toast.LENGTH_SHORT).show();
                                }
                            });
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        //NOTE: This is temporary implementation for testing for purposes.
        //Main flow should be to sign up user to firebase


    }

    private void initVars() {
        nameEdit = findViewById(R.id.name_edit);
        emailEdit = findViewById(R.id.email_edit);
        mobileEdit = findViewById(R.id.mobile_edit);
        passwordEdit = findViewById(R.id.password_edit);
        confirmPassEdit = findViewById(R.id.cnf_pass_edit);
        signUpProgress = findViewById(R.id.sign_up_progress);
        saveBtn = findViewById(R.id.save_button);

        //getting toolbar to get the back button
        Toolbar toolbar = findViewById(R.id.toolbar);
        backBtn = toolbar.findViewById(R.id.backBtn);

    }


    private void reload() {
    }
    private void updateUI(FirebaseUser user) {

    }


    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            reload();
        }
    }
}