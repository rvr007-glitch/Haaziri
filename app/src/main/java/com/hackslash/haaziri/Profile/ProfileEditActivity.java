package com.hackslash.haaziri.Profile;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hackslash.haaziri.R;
import com.hackslash.haaziri.activitydialog.ActivityDialog;
import com.hackslash.haaziri.firebase.FirebaseVars;
import com.hackslash.haaziri.models.UserProfile;

public class ProfileEditActivity extends AppCompatActivity {

    private static final String TAG = "ProfileEditActivity";

    //we name the variable in lowerCamelCase
    private Button saveBtn;
    private ImageView backBtn;
    private TextInputEditText emailEdit;
    private TextInputEditText phoneEdit;
    private TextInputEditText nameEdit;
    private ActivityDialog dialog;

    String email;
    String phone;
    String name;
    Editable new_email;
    Editable new_phone;
    Editable new_name;
    String id;

    private String userProfilePath;


    private Context mContext = this;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        //making status bar white with black icons
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //function to initialize all the layout variables, should be present in all activities
        initVars();

        //function to setup listeners, should be present in all activities
        setupListeners();

        //function to populate existing data in edit text fields
        getExistingData();
    }

    private void getExistingData() {
        FirebaseUser user= mAuth.getCurrentUser();

        id = user.getUid();
        userProfilePath = "/users/"+id+"/profile/";
        FirebaseVars.mRootRef.child(userProfilePath).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dialog.hideDialog();
                //user existing data fetched from database
                UserProfile profile = snapshot.getValue(UserProfile.class);
                //feeding existing data
                name = profile.getName();
                email = profile.getEmail();
                phone = profile.getMobile();
                nameEdit.setText(name);
                emailEdit.setText(email);
                phoneEdit.setText(phone);
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                //error occurred in fetching data
                dialog.hideDialog();
                Toast.makeText(mContext, "Error in fetching data", Toast.LENGTH_SHORT).show();
                Log.d(TAG, error.getMessage());
            }
        });
    }

    //This method is of no use since it is fetching data from database
//    private void Editinform() {
//
//
//
//        FirebaseUser user= mAuth.getCurrentUser();
//
//        id = user.getUid();
//        String path = "/users/"+id+"/profile/";
//        email = user.getEmail();
//        phone = user.getPhoneNumber();
//        name = user.getDisplayName();
//        new_email = Email.getText();
//        new_phone =  Phone.getText();
//        new_name =  Name.getText();
//
//    }

    private void initVars() {
        saveBtn = findViewById(R.id.Save);
        //getting toolbar to get the back button
        Toolbar toolbar = findViewById(R.id.toolbar);
        backBtn = toolbar.findViewById(R.id.backBtn);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        emailEdit = findViewById(R.id.email);
        nameEdit = findViewById(R.id.name);
        phoneEdit = findViewById(R.id.mobile);

        //creating user dialog for letting user know that we are loading their existing data
        dialog = new ActivityDialog(mContext);
        dialog.setTitle("Fetching Existing Data");
        dialog.setMessage("Please wait while we get your existing profile data");
        dialog.setCancelable(false);
        dialog.showDialog();
    }

    private void setupListeners() {
        backBtn.setOnClickListener(v -> finish());

        saveBtn.setOnClickListener(v -> saveNew());

}

    //we also write function name in lowerCamelCase
    //we write class name in UpperCamelCase
    private void saveNew() {

        //checking any data was edited using a function
        if(!isDataEdited()) return;
        UserProfile updatedProfile = new UserProfile(name, email, phone);

        //showing progress dialog to let user know that we are updating their profile
        dialog.setTitle("Updating Your Profile");
        dialog.setMessage("Please wait while we update your profile data");
        dialog.showDialog();

        //updating data in database
        FirebaseVars.mRootRef.child(userProfilePath).setValue(updatedProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull  Task<Void> task) {
                dialog.hideDialog();
                if(task.isSuccessful()){
                    //profile update complete in database
                    Toast.makeText(mContext, "Your profile has been updated", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    //profile updation failed in database
                    Toast.makeText(mContext, "Some error occurred in updating the profile", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, task.getException().getMessage());
                }
            }
        });
    }

    private boolean isDataEdited() {
        String newName = nameEdit.getText().toString();
        String newPhone = phoneEdit.getText().toString();
        //NOTE: We are not letting user to update email right now

        //checking if any of the required input field is empty or not
        if (newName.isEmpty()){
            nameEdit.setError("Name can't be empty");
            return false;
        }
        if (newPhone.length() != 10){
            phoneEdit.setError("Enter a valid 10 digit mobile number");
            return false;
        }
        //checking is any of the field was changed or not by matching the existing ones
        if (newName.equals(name)  && newPhone.equals(phone)){
            finish();
            return false;
        }

        //if all checks are done, updating the data in local variables
        name = newName;
        phone = newPhone;
        return true;
    }
}