package com.hackslash.haaziri.onboarding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hackslash.haaziri.Profile.ProfileActivity;
import com.hackslash.haaziri.R;
import com.hackslash.haaziri.activitydialog.ActivityDialog;

public class LoginActivity extends AppCompatActivity {

    private final Context mContext = this;

    Button loginBtn;
    TextView forgotPasswordBtn;
    TextView createOneBtn;
    EditText emailTxt, passwordTxt;
    private FirebaseAuth firebaseAuth;
    //an custom activity dialog to show user progress for its activities
    private ActivityDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //hiding the action bar
        getSupportActionBar().hide();

        //making status bar white with black icons
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //function to initialize all the layout variables, should be present in all activities
        initVars();

        //function to setup listeners, should be present in all activities
        setupListeners();

    }



    private void setupListeners() {
        //creating login system and firebase authentication
         loginBtn.setOnClickListener(v -> {
             String email = emailTxt.getText().toString().trim();
             String password = passwordTxt.getText().toString().trim();


             //checking if email password are filled or empty
             if (TextUtils.isEmpty(email)) {
                 //setting empty email error to the email edit text field
                 emailTxt.setError("Please enter an email");
                 return;
             }
             if (TextUtils.isEmpty(password)) {
                 //setting empty password error to the password edit text field
                 passwordTxt.setError("Please enter a password first");
                 return;

             }


             loginUser(email, password);
       });
        forgotPasswordBtn.setOnClickListener(v -> {
          Toast.makeText(this, "forgot password clicked", Toast.LENGTH_SHORT).show();
       });
       createOneBtn.setOnClickListener(v -> {
           sendToRegisterActivity();
      });
    }

    private void loginUser(String email, String password){
        dialog.setTitle("Logging You in");
        dialog.setMessage("Please wait while we log you in");
        dialog.setCancelable(false);
        dialog.showDialog();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.hideDialog();
                        if (task.isSuccessful()) {
                            //checking the user verify his/her email or not
                            final FirebaseUser user =firebaseAuth.getCurrentUser();
                            if(user.isEmailVerified()){
                                startActivity(new Intent(mContext, ProfileActivity.class));
                                finish();
                            }
                            if(!user.isEmailVerified()){
                                dialog.setTitle("Sending Verification Email");
                                dialog.setMessage("Your email is not verified, we are resending you verification email");
                                dialog.showDialog();
                                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        dialog.hideDialog();
                                        Toast.makeText(LoginActivity.this, "Verify your email from the sent email",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        } else {

                            Toast.makeText(LoginActivity.this, " Login Failed or User doesn't exists", Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }

    private void sendToRegisterActivity() {
        Intent intent = new Intent(mContext, SignUpActivity.class);
        startActivity(intent);
    }

    private void initVars() {
        loginBtn = findViewById(R.id.login_button);
        forgotPasswordBtn = findViewById(R.id.forget_password);
        createOneBtn = findViewById(R.id.create_one);
        // initialization of Edit field and firebase
        emailTxt = findViewById(R.id.textInputEditTextEmail);
        passwordTxt = findViewById(R.id.textInputEditTextPassword);
        firebaseAuth = FirebaseAuth.getInstance();
        dialog = new ActivityDialog(mContext);
    }
}