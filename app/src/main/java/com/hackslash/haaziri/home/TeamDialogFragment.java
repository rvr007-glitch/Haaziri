package com.hackslash.haaziri.home;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.hackslash.haaziri.R;
import com.hackslash.haaziri.activitydialog.ActivityDialog;
import com.hackslash.haaziri.firebase.FirebaseVars;
import com.hackslash.haaziri.models.Team;
import com.hackslash.haaziri.utils.MotionToastUtitls;

import javax.xml.XMLConstants;


public class TeamDialogFragment extends DialogFragment {

    private static final String TAG = "TeamDialogFragment";

    private View rootView;
    private EditText teamCodeEdit;
    private Button joinTeamBtn;
    private EditText teamNameEdit;
    private Button createTeamBtn;
    private ActivityDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =inflater.inflate(R.layout.fragment_add_join_team, container, false);
        if (getDialog() != null && getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initVars();

        setupListeners();
        return rootView;
    }

    private void setupListeners() {
        joinTeamBtn.setOnClickListener(v -> {
            MotionToastUtitls.showInfoToast(getContext(), "Join Team", "Join team");
        });

        createTeamBtn.setOnClickListener(v -> {
            String teamName = teamNameEdit.getText().toString();
            if (teamName.length() <=2){
                MotionToastUtitls.showWarningDialog(getContext(), "Warning", "Team name should be of more than 2 characters long");
                return;
            }
            createNewTeam(teamName);
        });
    }

    private void createNewTeam(String teamName) {
        progressDialog.setTitle("Creating new team");
        progressDialog.setMessage("Please hold on until we create your team");
        progressDialog.showDialog();
        String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String teamId = teamName.substring(0, 2).toUpperCase();
        teamId += System.currentTimeMillis()%1000000;
        Team newTeam = new Team(teamName, teamId, UID);
        String finalTeamId = teamId;
        FirebaseVars.mRootRef.child("/teams/"+teamId+"/").setValue(newTeam).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    saveToUserNode(UID, finalTeamId);
                }else{
                    progressDialog.hideDialog();
                    MotionToastUtitls.showErrorToast(getContext(), "Error", "Some error occurred while creating team");
                    Log.d(TAG, task.getException().toString());
                }
            }
        });
    }

    private void saveToUserNode(String UID, String teamId) {
        FirebaseVars.mRootRef.child("/users/"+UID+"/team/owned/").push().setValue(teamId).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.hideDialog();
                    MotionToastUtitls.showSuccessToast(getContext(), "Team Created", "Your team has been created successfully");
                }else{
                    progressDialog.hideDialog();
                    MotionToastUtitls.showErrorToast(getContext(), "Error", "Some error occurred while creating team");
                    Log.d(TAG, task.getException().toString());
                }
            }
        });
    }

    private void initVars() {
        teamCodeEdit = rootView.findViewById(R.id.teamCodeEdit);
        joinTeamBtn = rootView.findViewById(R.id.joinTeamBtn);
        teamNameEdit = rootView.findViewById(R.id.teamNameEdit);
        createTeamBtn = rootView.findViewById(R.id.createTeamBtn);
        progressDialog = new ActivityDialog(getContext());
        progressDialog.setCancelable(false);
    }
}