package com.hackslash.haaziri.home;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintSet;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.hackslash.haaziri.R;
import com.hackslash.haaziri.activitydialog.ActivityDialog;
import com.hackslash.haaziri.firebase.FirebaseVars;
import com.hackslash.haaziri.models.Team;
import com.hackslash.haaziri.utils.MotionToastUtitls;

import java.util.HashMap;

import javax.xml.XMLConstants;


public class TeamDialogFragment extends DialogFragment {

    private static final String TAG = "TeamDialogFragment";

    private View rootView;
    private EditText teamCodeEdit;
    private Button joinTeamBtn;
    private EditText teamNameEdit;
    private Button createTeamBtn;
    private ActivityDialog progressDialog;

    private String UID;
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
            String teamCode = teamCodeEdit.getText().toString();
            if (teamCode.isEmpty()){
                MotionToastUtitls.showWarningDialog(getContext(), "Warning", "Team code cannot be empty");
                return;
            }
            searchTeam(teamCode);
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

    private void searchTeam(String teamCode) {
        progressDialog.setTitle("Searching for team");
        progressDialog.setMessage("Please wait while we find your team");
        progressDialog.showDialog();
        FirebaseVars.mRootRef.child("/teams/").orderByChild("teamCode").equalTo(teamCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    checkIfUserExists(snapshot.child(teamCode));
                }else {
                    progressDialog.hideDialog();
                    MotionToastUtitls.showErrorToast(getContext(), "Team not found", "No team exists with provided code, please recheck");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.hideDialog();
                MotionToastUtitls.showErrorToast(getContext(), "Error", "Some internal error occurred, try after some time");
                Log.d(TAG, error.getMessage());
            }
        });
    }

    /**
     * Function that checks whether the current user is already in the team
     * or is the owner of the team
     * @param snapshot  Snapshot object of the found team from the database
     */
    private void checkIfUserExists(DataSnapshot snapshot) {
        progressDialog.setTitle("Team found, adding you");
        progressDialog.setMessage("We found your team, please hold on until we add you to the team");
        Team team = snapshot.getValue(Team.class);

        //check if the user is already the owner of the team
        if (team.getTeamOwnerUid().equals(UID)){
            progressDialog.hideDialog();
            MotionToastUtitls.showInfoToast(getContext(), "Info", "You are already the owner of the team");
            return;
        }

        //check if user already exists in the member list of the team
        if (snapshot.child("members").exists()){
            for (DataSnapshot child: snapshot.child("members").getChildren()){
                String id = child.getValue(String.class);
                if (id.equals(UID)){
                    progressDialog.hideDialog();
                    MotionToastUtitls.showInfoToast(getContext(), "Info", "You are already a member of this team");
                    return;
                }
            }
        }

        addUserToTeam(snapshot);
    }

    private void addUserToTeam(DataSnapshot snapshot) {
        String teamCode = snapshot.child("teamCode").getValue(String.class);
        FirebaseVars.mRootRef.child("/teams/"+teamCode+"/members/").push().setValue(UID).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                addTeamToUser(teamCode);
            }
        });
    }

    private void addTeamToUser(String teamCode) {
        FirebaseVars.mRootRef.child("/users/"+UID+"/team/joined/").push().setValue(teamCode).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.hideDialog();
                MotionToastUtitls.showSuccessToast(getContext(), "Your are in", "You have been successfully added to the team");
                getDialog().dismiss();
            }
        });
    }


    /**
     * Function that creates new team in the database
     * @param teamName Name of the team to be created
     */
    private void createNewTeam(String teamName) {
        progressDialog.setTitle("Creating new team");
        progressDialog.setMessage("Please hold on until we create your team");
        progressDialog.showDialog();
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
        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}