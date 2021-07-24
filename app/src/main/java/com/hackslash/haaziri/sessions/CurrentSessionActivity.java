package com.hackslash.haaziri.sessions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hackslash.haaziri.R;
import com.hackslash.haaziri.activitydialog.ActivityDialog;
import com.hackslash.haaziri.firebase.FirebaseVars;
import com.hackslash.haaziri.utils.Constants;

public class CurrentSessionActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button stopBtn;
    private TextView sessionIdTv;
    private RecyclerView attendenceRecycler;
    private ImageView backBtn;
    private String teamCode = "";
    private String teamName = "";
    private String sessionId = "";

    private ActivityDialog dialog;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_session);

        //making status bar white with black icons
        getWindow().setStatusBarColor(getColor(R.color.home_status_bar_color));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //getting incoming intent
        Intent intent = getIntent();
        teamCode = intent.getStringExtra(Constants.TEAM_CODE_KEY);
        teamName = intent.getStringExtra(Constants.TEAM_NAME_KEY);
        sessionId = intent.getStringExtra(Constants.SESSION_CODE_KEY);

        toolbar = findViewById(R.id.current_toolbar);
        //setting toolbar title to selected team title
        TextView titleTv = toolbar.findViewById(R.id.titleTv);
        titleTv.setText(teamName);
        setSupportActionBar(toolbar);

        initVars();

        setupListeners();
    }

    private void setupListeners() {
        backBtn.setOnClickListener(v -> closeSession());
        stopBtn.setOnClickListener(v -> closeSession());
    }

    private void closeSession() {
        dialog.setTitle("Stopping session");
        dialog.setMessage("Please wait while we wrap up your session and save the details");
        dialog.showDialog();
        //turning off bluetooth
        final BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = manager.getAdapter();
        bluetoothAdapter.disable();
        //stopping session from backend by removing id
        FirebaseVars.mRootRef.child("/teams/" + teamCode + "/currentSessionId/").setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dialog.hideDialog();
                finish();
            }
        });
        finish();
    }

    private void initVars() {
        stopBtn = findViewById(R.id.stopbtn);
        sessionIdTv = findViewById(R.id.sesstxtv);
        sessionIdTv.setText("Session Id: " + sessionId);
        attendenceRecycler = findViewById(R.id.attendance_recyclerview);
        backBtn = toolbar.findViewById(R.id.backBtn);
        dialog = new ActivityDialog(mContext);
        dialog.setCancelable(false);

    }
}