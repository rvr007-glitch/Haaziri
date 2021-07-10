package com.hackslash.haaziri.splash;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.hackslash.haaziri.MainActivity;
import com.hackslash.haaziri.Profile.ProfileActivity;
import com.hackslash.haaziri.R;
import com.hackslash.haaziri.firebase.FirebaseVars;
import com.hackslash.haaziri.home.HomeScreenActivity;
import com.hackslash.haaziri.intro.IntroSliderActivity;
import com.hackslash.haaziri.onboarding.LoginActivity;
import com.hackslash.haaziri.utils.MotionToastUtitls;

import java.util.function.IntToDoubleFunction;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_TIME = 3000;
    private Context mContext = this;

    private ImageView splashLogo;
    private ImageView splashLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        //variable the contains the root reference of the firebase database
        FirebaseVars.mRootRef = FirebaseDatabase.getInstance().getReference();
        //making status bar white only
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));

        splashLabel = findViewById(R.id.splashLabel);
        splashLogo = findViewById(R.id.splashLogo);
        new Handler().postDelayed(() -> {
            //starting splash animation
            splashLabel.animate().scaleX(0.0f);
            splashLogo.animate().scaleX(0.0f).scaleY(0.0f).withEndAction(() -> {
                //Intent for start mainActivity
                Intent splashIntent;
                //checking if user has logged in previously
                if (mAuth.getCurrentUser() != null) {
                    MotionToastUtitls.showSuccessToast(mContext, "Welcome back", "Glad to see you");
                    splashIntent = new Intent(mContext, HomeScreenActivity.class);
                } else
                    splashIntent = new Intent(mContext, IntroSliderActivity.class);
                startActivity(splashIntent);
                finish();
            });
        }, SPLASH_DISPLAY_TIME);

    }
}