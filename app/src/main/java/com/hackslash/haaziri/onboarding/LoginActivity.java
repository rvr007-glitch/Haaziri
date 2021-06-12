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

public class LoginActivity extends AppCompatActivity {

    private final Context mContext = this;

    Button loginBtn;
    TextView forgotPasswordBtn;
    TextView createOneBtn;
    EditText emailTxt, passwordTxt;
    private FirebaseAuth firebaseAuth;

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

        //creating login system and firebase authentication
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailTxt.getText().toString().trim();
                String password = passwordTxt.getText().toString().trim();


                //tost message for email and password
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    return;

                }


                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //checking the user verify his/her email or not
                                  final FirebaseUser user =firebaseAuth.getCurrentUser();
                                    if(user.isEmailVerified()){
                                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                                    }
                                    if(!user.isEmailVerified()){
                                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(LoginActivity.this, " Verify your Email first",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                } else {

                                    Toast.makeText(LoginActivity.this, " Login Failed or User Not Regester", Toast.LENGTH_SHORT).show();

                                }

                            }
                        });

            }
        });
    }



    private void setupListeners() {
        /* loginBtn.setOnClickListener(v -> {
           Toast.makeText(this, "Login button clicked", Toast.LENGTH_SHORT).show();
       }); */
        forgotPasswordBtn.setOnClickListener(v -> {
          Toast.makeText(this, "forgot password clicked", Toast.LENGTH_SHORT).show();
       });
       createOneBtn.setOnClickListener(v -> {
           sendToRegisterActivity();
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
    }
}