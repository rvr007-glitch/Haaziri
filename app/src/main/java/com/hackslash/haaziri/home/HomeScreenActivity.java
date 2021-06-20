package com.hackslash.haaziri.home;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hackslash.haaziri.Profile.ProfileActivity;
import com.hackslash.haaziri.R;
import com.hackslash.haaziri.activitydialog.ActivityDialog;
import com.hackslash.haaziri.firebase.FirebaseVars;
import com.hackslash.haaziri.models.UserProfile;

public class HomeScreenActivity extends AppCompatActivity {

    private static final String TAG = "HomeScreenActivity";

    private Context mContext = this;

    private ImageView profileBtn;
    private ImageButton addBtn;
    private TextView toolbarNameTv;
    private ActivityDialog dialog;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //making status bar white with black icons
        getWindow().setStatusBarColor(getColor(R.color.home_status_bar_color));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //function to initialize all the layout variables, should be present in all activities
        initVars();

        //function to setup listeners, should be present in all activities
        setupListeners();

        //function to fetch data
        fetchData();
    }

    private void fetchData() {
        if(FirebaseVars.mRootRef == null) return;
        String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String path = "/users/"+UID+"/";
        FirebaseVars.mRootRef.child(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //user data fetched successfully
                dialog.hideDialog();
                UserProfile currentUserProfile = snapshot.child("profile").getValue(UserProfile.class);
                //getting user first name to show in toolbar
                String firstName = currentUserProfile.getName().split(" ")[0];
                toolbarNameTv.setText("Hi "+firstName+"!");
                toolbarNameTv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Unable to fetch data
                dialog.hideDialog();
                Toast.makeText(mContext, "Error occurred while fetching details", Toast.LENGTH_SHORT).show();
                Log.d(TAG, error.getMessage());
            }
        });
    }

    private void setupListeners() {
        profileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, ProfileActivity.class);
            startActivity(intent);
        });
        addBtn.setOnClickListener(v -> {
            Toast.makeText(mContext, "Add Team Button pressed", Toast.LENGTH_SHORT).show();
        });
    }

    private void initVars() {
        addBtn = findViewById(R.id.addBtn);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        profileBtn = toolbar.findViewById(R.id.profileBtn);
        toolbarNameTv = toolbar.findViewById(R.id.toolbarUserText);
        dialog = new ActivityDialog(mContext);
        dialog.setTitle("Fetching Details");
        dialog.setMessage("Please wait while we fetch your details");
        dialog.setCancelable(false);
        dialog.showDialog();
    }
}