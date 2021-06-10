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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hackslash.haaziri.R;
import com.hackslash.haaziri.onboarding.LoginActivity;

public class ProfileActivity extends AppCompatActivity {

    private Context mContext = this;


    LinearLayout logoutBtn;
    ImageView backBtn;
   TextView tname=(TextView)findViewById(R.id.textView);
   TextView temail=(TextView)findViewById(R.id.editTextNumber3);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //making status bar white with black icons
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initVars();
        setupListeners();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            tname.setText(name);
            temail.setText(email);
        }

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