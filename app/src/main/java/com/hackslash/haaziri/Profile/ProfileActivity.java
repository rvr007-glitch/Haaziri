package com.hackslash.haaziri.Profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hackslash.haaziri.R;
import com.hackslash.haaziri.onboarding.LoginActivity;

public class ProfileActivity extends AppCompatActivity {

    private Context mContext = this;

    LinearLayout logoutBtn;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //making status bar white with black icons
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initVars();
        setupListeners();
    }

    private void setupListeners() {
        backBtn.setOnClickListener(v -> finish());

        //NOTE; This is temporary implementation for logout button
        logoutBtn.setOnClickListener(v -> {
            logoutUser();
        });
    }

    private void logoutUser() {
        //starting login activity and finishing all previous activities for security purposes.
        Toast.makeText(mContext, "Logged out", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(mContext, LoginActivity.class));
        finishAffinity();
    }

    private void initVars() {
        logoutBtn = findViewById(R.id.logout_btn);
        //getting toolbar to get the back button
        Toolbar toolbar = findViewById(R.id.toolbar);
        backBtn = toolbar.findViewById(R.id.backBtn);
    }
}