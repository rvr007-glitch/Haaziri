package com.hackslash.haaziri.onboarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hackslash.haaziri.Profile.ProfileActivity;
import com.hackslash.haaziri.R;

public class SignUpActivity extends AppCompatActivity {

    private Context mContext = this;

    EditText nameEdit;
    EditText emailEdit;
    EditText mobileEdit;
    EditText passwordEdit;
    EditText confirmPassEdit;
    ProgressBar signUpProgress;
    Button saveBtn;
    ImageView backBtn;

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

        //function to setup listeners, should be present in all activities
        setupListeners();
    }

    private void setupListeners() {
        saveBtn.setOnClickListener(v -> {
           signupUser();
        });
        backBtn.setOnClickListener(v -> finish());
    }

    private void signupUser() {
        //NOTE: This is temporary implementation for testing for purposes.
        //Main flow should be to sign up user to firebase
        Intent temporaryIntent = new Intent(mContext, ProfileActivity.class);
        startActivity(temporaryIntent);
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
}