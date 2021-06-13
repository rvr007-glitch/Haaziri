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
    //don't initialize your views here, initialize it in initVars function
   TextView tname;
   TextView temail;
   private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //making status bar white with black icons
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initVars();
        setupListeners();
        //separating UI creation to another function for more cleaner code and easy debugging
        updateUI();

    }

    private void updateUI() {
        FirebaseUser user= mAuth.getCurrentUser();
        if(user!=null) {
            String name = (user.getDisplayName() != null ?user.getDisplayName():"Test User");
            String email = user.getEmail();
            tname.setText(name);
            temail.setText(email);
        }
    }

    //function for setting the click listeners for required views
    //called single time from onCreate method
    private void setupListeners() {
        backBtn.setOnClickListener(v -> finish());

        //NOTE; This is temporary implementation for logout button
        logoutBtn.setOnClickListener(v -> {
            logoutUser();
        });
    }

    //function for logging user out
    private void logoutUser() {
        /*directly calling sign out to Firebase auth instance
        which would sign out the current logged user from device*/
        mAuth.signOut();
        //starting login activity and finishing all previous activities for security purposes.
        Toast.makeText(mContext, "Logged out", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(mContext, LoginActivity.class));
        finishAffinity();
    }

    //function to initialize all the view, called from onCreate method
    private void initVars() {
        logoutBtn = findViewById(R.id.logout_btn);
        //getting toolbar to get the back button
        Toolbar toolbar = findViewById(R.id.toolbar);
        backBtn = toolbar.findViewById(R.id.backBtn);
        mAuth = FirebaseAuth.getInstance();
        tname = findViewById(R.id.nameTv);
        temail = findViewById(R.id.emailTv);
    }
}