package com.hackslash.haaziri.Profile;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hackslash.haaziri.R;

public class ProfileEditActivity extends AppCompatActivity {

    Button Save;
    ImageView backBtn;
    TextInputEditText Email;
    TextInputEditText Phone;
    TextInputEditText Name;

    String email;
    String phone;
    String name;
    Editable new_email;
    Editable new_phone;
    Editable new_name;
    String id;


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

        //to change the information
        Editinform();
    }

    private void Editinform() {



        FirebaseUser user= mAuth.getCurrentUser();

        id = user.getUid();
        String path = "/users/"+id+"/profile/";
        email = user.getEmail();
        phone = user.getPhoneNumber();
        name = user.getDisplayName();
        new_email = Email.getText();
        new_phone =  Phone.getText();
        new_name =  Name.getText();

    }

    private void initVars() {
        Save = findViewById(R.id.Save);
        //getting toolbar to get the back button
        Toolbar toolbar = findViewById(R.id.toolbar);
        backBtn = toolbar.findViewById(R.id.backBtn);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Email = findViewById(R.id.email);
        Name = findViewById(R.id.name);
        Phone = findViewById(R.id.mobile);
    }

    private void setupListeners() {
        backBtn.setOnClickListener(v -> finish());


        Save.setOnClickListener(v -> Savenew());

}

    private void Savenew() {

        if (!name.isEmpty())
        {
            if(!name.equals(new_name))
            {
                mDatabase.child("/users/"+id+"/profile/name").setValue(new_name);
            }
        }
        else
        {
            Toast.makeText(mContext, "Blank fields not allowed", Toast.LENGTH_SHORT).show();
        }

        if (!email.isEmpty())
        {
            if(!email.equals(new_email))
            {
                mDatabase.child("/users/"+id+"/profile/email").setValue(new_email);
            }
        }
        else
        {
            Toast.makeText(mContext, "Blank fields not allowed", Toast.LENGTH_SHORT).show();
        }

        if (!phone.isEmpty())
        {
            if(!phone.equals(new_phone))
            {
                mDatabase.child("/users/"+id+"/profile/mobile").setValue(new_phone);
            }
        }
        else
        {
            Toast.makeText(mContext, "Blank fields not allowed", Toast.LENGTH_SHORT).show();
        }
        if (phone.equals(new_phone) && email.equals(new_email) && name.equals(new_name))
        {
            Toast.makeText(mContext, "No Changes", Toast.LENGTH_SHORT).show();
        }


    }
    }