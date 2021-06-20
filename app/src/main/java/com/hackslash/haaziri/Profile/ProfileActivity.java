package com.hackslash.haaziri.Profile;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hackslash.haaziri.R;
import com.hackslash.haaziri.activitydialog.ActivityDialog;
import com.hackslash.haaziri.onboarding.LoginActivity;

public class ProfileActivity extends AppCompatActivity {

    private Context mContext = this;


    //Remember to declare variables or view as private if they are to be only used in single activity
    private LinearLayout logoutBtn;
    private ImageView backBtn;
    //don't initialize your views here, initialize it in initVars function
    private TextView tname;
    private TextView temail;
    private TextView phno;
    private ImageView editProfileBtn;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference reference;

    //creating a progress dialog to let user know about data fetching
    private ActivityDialog dialog;


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

    @Override
    protected void onStart() {
        super.onStart();

        //calling getdata to ensure that the updated data is always shown
        getdata();
    }

    private void updateUI() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String name = (user.getDisplayName() != null ? user.getDisplayName() : "Test User");
            String email = user.getEmail();
            tname.setText(name);
            temail.setText(email);
        }
    }

    //function for setting the click listeners for required views
    //called single time from onCreate method
    private void setupListeners() {
        backBtn.setOnClickListener(v -> finish());

        logoutBtn.setOnClickListener(v -> {
            logoutUser();
        });

        editProfileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, ProfileEditActivity.class);
            startActivity(intent);
                }
        );
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

    private void getdata() {
        //setting up progress dialog to let user know about data fetching operation
        dialog.setTitle("Fetching Profile Data");
        dialog.setMessage("Please wait while we fetch your profile data");
        dialog.setCancelable(false);
        dialog.showDialog();

        currentUser = mAuth.getCurrentUser();
        String userUid = currentUser.getUid();
        String userProfilePath = "/users/" + userUid + "/profile/";
        reference = FirebaseDatabase.getInstance().getReference(userProfilePath);
        reference.addListenerForSingleValueEvent(listener);

    }

    ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            dialog.hideDialog();
            if (snapshot.exists()) {
                String name = snapshot.child("name").getValue().toString();
                String email = snapshot.child("email").getValue().toString();
                String ph = snapshot.child("mobile").getValue().toString();
                tname.setText(name);
                temail.setText(email);
                phno.setText(ph);
            } else {
                Toast.makeText(mContext, "No Record Found", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            dialog.hideDialog();
            Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
        }
    };

    //function to initialize all the view, called from onCreate method
    private void initVars() {
        logoutBtn = findViewById(R.id.logout_btn);
        //getting toolbar to get the back button
        Toolbar toolbar = findViewById(R.id.toolbar);
        backBtn = toolbar.findViewById(R.id.backBtn);
        mAuth = FirebaseAuth.getInstance();
        tname = (TextView) findViewById(R.id.nameTv);
        temail = (TextView) findViewById(R.id.emailTv);
        phno = (TextView) findViewById(R.id.phoneTv);
        editProfileBtn = findViewById(R.id.editProfileBtn);

        dialog = new ActivityDialog(mContext);
    }
}