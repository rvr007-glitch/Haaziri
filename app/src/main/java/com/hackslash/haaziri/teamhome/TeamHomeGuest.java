package com.hackslash.haaziri.teamhome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.hackslash.haaziri.R;
import com.hackslash.haaziri.activitydialog.ActivityDialog;
import com.hackslash.haaziri.firebase.FirebaseVars;
import com.hackslash.haaziri.models.Team;
import com.hackslash.haaziri.utils.Constants;
import com.hackslash.haaziri.utils.MotionToastUtitls;

public class TeamHomeGuest extends AppCompatActivity {

    private static final String TAG = "TeamHomeGuest";

    private Button giveHaaziriBtn;
    private String teamCode = "";
    private ActivityDialog dialog;
    private Context mContext = this;
    private String currentSessionId = "";
    private Team currentTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_home_guest);

        //making status bar white with black icons
        getWindow().setStatusBarColor(getColor(R.color.home_status_bar_color));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initVars();

        setupListeners();

        fetchTeamDetails();
    }

    private void fetchTeamDetails() {
        dialog.setTitle("Fetching team details");
        dialog.setMessage("Please wait while we fetch the team details");
        dialog.showDialog();
        FirebaseVars.mRootRef.child("/teams/" + teamCode + "/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dialog.hideDialog();
                currentSessionId = snapshot.child("currentSessionId").getValue(String.class);
                currentTeam = snapshot.getValue(Team.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.hideDialog();
                MotionToastUtitls.showErrorToast(mContext, "Error", "Some error occurred in fetching current session details");
                Log.d(TAG, "onCancelled: " + error.getDetails());
            }
        });
    }

    private void setupListeners() {
        giveHaaziriBtn.setOnClickListener(v -> {
            if (currentSessionId == null || currentSessionId.isEmpty()) {
                MotionToastUtitls.showInfoToast(mContext, "No session going on", "Currently no session is going on");
            } else {
                MotionToastUtitls.showSuccessToast(mContext, "Session on going", "Session is going on");
            }
        });
    }


    private void initVars() {
        Intent intent = getIntent();
        teamCode = intent.getStringExtra(Constants.TEAM_CODE_KEY);
        giveHaaziriBtn = findViewById(R.id.giveHaaziriBtn);
        dialog = new ActivityDialog(mContext);
        dialog.setCancelable(false);
    }
}