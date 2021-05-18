package com.hackslash.haaziri.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.hackslash.haaziri.MainActivity;
import com.hackslash.haaziri.R;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Intent for start mainActivity
                Intent splashIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(splashIntent);
                finish();
            }
        }, SPLASH_DISPLAY_TIME);

    }
}