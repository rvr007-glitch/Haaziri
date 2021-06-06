package com.hackslash.haaziri.onboarding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hackslash.haaziri.R;

public class LoginActivity extends AppCompatActivity {

    private final Context mContext = this;

    Button loginBtn;
    TextView forgotPasswordBtn;
    TextView createOneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        loginBtn.setOnClickListener(v -> {
            Toast.makeText(this, "Login button clicked", Toast.LENGTH_SHORT).show();
        });
        forgotPasswordBtn.setOnClickListener(v -> {
            Toast.makeText(this, "forgot password clicked", Toast.LENGTH_SHORT).show();
        });
        createOneBtn.setOnClickListener(v -> {
            sendToRegisterActivity();
        });
    }

    private void sendToRegisterActivity() {
        Intent intent = new Intent(mContext, SignUpActivity.class);
        startActivity(intent);
    }

    private void initVars() {
        loginBtn = findViewById(R.id.login_button);
        forgotPasswordBtn = findViewById(R.id.forget_password);
        createOneBtn = findViewById(R.id.create_one);
    }
}