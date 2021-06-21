package com.hackslash.haaziri.onboarding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hackslash.haaziri.Profile.ProfileActivity;
import com.hackslash.haaziri.R;
import com.hackslash.haaziri.activitydialog.ActivityDialog;
import com.hackslash.haaziri.models.UserProfile;
import com.hackslash.haaziri.utils.MotionToastUtitls;

public class SignUpActivity extends AppCompatActivity {

    private Context mContext = this;

    EditText nameEdit;
    String email;
    String password;
    String name;
    String phone;
    String confirmPass;
    EditText emailEdit;
    EditText mobileEdit;
    EditText passwordEdit;
    EditText confirmPassEdit;
    ProgressBar signUpProgress;
    Button saveBtn;
    ImageView backBtn;
    private FirebaseAuth mAuth;
    private static final String TAG = "AnonymousAuth";
    private boolean len=false,hasUppercase=false,hasNumber=false,isRegistrationClickable=false;
    private ActivityDialog dialog;

    //variable the current signed up user reference
    private FirebaseUser currentUser;

    //variable that stores the root reference of the database
    private DatabaseReference mRootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //disabling dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //hiding the action bar
        getSupportActionBar().hide();

        //making status bar white with black icons
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //function to initialize all the layout variables, should be present in all activities
        initVars();



        mAuth = FirebaseAuth.getInstance();



        //function to setup listeners, should be present in all activities
        setupListeners();
    }

    private void setupListeners() {
        saveBtn.setOnClickListener(v -> {
            //if(email and password pattern matches) ->signUpUser
            //else-> show error toast
            name = nameEdit.getText().toString();
            email=  emailEdit.getText().toString();
            password=passwordEdit.getText().toString();
            phone = mobileEdit.getText().toString();
            confirmPass = confirmPassEdit.getText().toString();

            //checking if user has filled all user fields
            if(!(checkEntries())) return;

            if (password.length() > 6) {
                len = true;
            }
            //made changes to support small letter as well in password
            if (password.matches("(.*[A-Za-z].*)")) {
                hasUppercase = true;
            }
            if (password.matches("(.*[0-9].*)")) {
                hasNumber = true;
            }
            //removed symbol regex for password since firebase forgot password features does not have this
            if (len &&  hasNumber && hasUppercase && email.length() > 0) {

                signupUser();
            }
            else{
                MotionToastUtitls.showErrorToast(mContext, "Error", "Make sure your password is of length greater than 6 and is a combination of letters and numbers");
            }

        });
        backBtn.setOnClickListener(v -> finish());
    }

    //function to check if user has entered all the details
    private boolean checkEntries() {
        if(name.isEmpty()){
            nameEdit.setError("Please enter your name");
            return false;
        }
        if(email.isEmpty()){
            emailEdit.setError("Please enter your email");
            return false;
        }
        if(phone.length() != 10){
            mobileEdit.setError("Please a valid 10 digit mobile no.");
            return false;
        }
        if(password.isEmpty()){
            passwordEdit.setError("Please enter a password");
            return false;
        }
        if(confirmPass.isEmpty()){
            confirmPassEdit.setError("Please enter your confirmed password");
            return false;
        }

        //checking if password and confirm password match
        if(!(password.equals(confirmPass))){
            Toast.makeText(mContext, "Your password and confirmed password doesn't match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void signupUser() {
        //using custom progress dialog for better user experience
        dialog.setTitle("Signing up");
        dialog.setMessage("Please wait while we sign you up");
        dialog.setCancelable(false);
        dialog.showDialog();
        mAuth.createUserWithEmailAndPassword(email, password)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.hideDialog();
                        if (task.isSuccessful()) {
                            //saving details to database
                            currentUser = mAuth.getCurrentUser();
                            saveDetailsToDb();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed or user already exists",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        //NOTE: This is temporary implementation for testing for purposes.
        //Main flow should be to sign up user to firebase


    }

    private void saveDetailsToDb() {
        dialog.setTitle("Saving your details");
        dialog.setMessage("Please wait while we save your details");
        dialog.showDialog();
        UserProfile profile = new UserProfile(name, email, phone);
        String userUid = currentUser.getUid();
        String userProfilePath = "/users/"+userUid+"/profile/";
        mRootRef.child(userProfilePath).setValue(profile).addOnSuccessListener(unused -> {
            //sending verification email after successful data saving
            dialog.hideDialog();
            sendVerificatonEmail();
        }).addOnFailureListener(e -> {
            //showing error for failed data saving
            dialog.hideDialog();
            Toast.makeText(mContext, "Unable to proceed", Toast.LENGTH_SHORT).show();
            Log.d(TAG, e.getMessage());
        });

    }

    private void sendVerificatonEmail() {
        dialog.setTitle("Sending verification email");
        dialog.setMessage("We are sending a verification email to verify that its you.");
        dialog.showDialog();
        currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull  Task<Void> task) {
                //signing out user to prevent unintended access after sending verification email
                mAuth.signOut();
                dialog.hideDialog();
                MotionToastUtitls.showSuccessToast(mContext, "Sign up succesful", "Your account has been created successfully now please verify your email to login");
                finish();
            }
        });
    }

    private void initVars() {
        nameEdit = findViewById(R.id.name_edit);
        emailEdit = findViewById(R.id.email_edit);
        mobileEdit = findViewById(R.id.mobile_edit);
        passwordEdit = findViewById(R.id.password_edit);
        confirmPassEdit = findViewById(R.id.cnf_pass_edit);
        signUpProgress = findViewById(R.id.sign_up_progress);
        saveBtn = findViewById(R.id.save_button);

        //getting toolbar to get the back button
        Toolbar toolbar = findViewById(R.id.toolbar);
        backBtn = toolbar.findViewById(R.id.backBtn);

        //initializing the custom activity dialog
        dialog = new ActivityDialog(mContext);

        //getting database root reference
        mRootRef = FirebaseDatabase.getInstance().getReference();
    }


    private void reload() {
    }
    private void updateUI(FirebaseUser user) {

    }


    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            reload();
        }
    }
}