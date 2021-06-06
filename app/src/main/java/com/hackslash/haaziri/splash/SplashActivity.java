package com.hackslash.haaziri.splash;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.hackslash.haaziri.MainActivity;
import com.hackslash.haaziri.Profile.ProfileActivity;
import com.hackslash.haaziri.R;
import com.hackslash.haaziri.onboarding.LoginActivity;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_TIME = 3000;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //making status bar white only
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Intent for start mainActivity
                Intent splashIntent = new Intent(mContext, LoginActivity.class);
                startActivity(splashIntent);
                finish();
            }
        }, SPLASH_DISPLAY_TIME);

    }
}