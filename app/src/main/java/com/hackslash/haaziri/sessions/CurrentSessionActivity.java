package com.hackslash.haaziri.sessions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hackslash.haaziri.R;

public class CurrentSessionActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button stopBtn;
    private TextView sessionIdTv;
    private RecyclerView attendenceRecycler;
    private ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_session);

        //making status bar white with black icons
        getWindow().setStatusBarColor(getColor(R.color.home_status_bar_color));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        toolbar = findViewById(R.id.current_toolbar);
        setSupportActionBar(toolbar);

        initVars();

        setupListeners();
    }

    private void setupListeners() {
        backBtn.setOnClickListener(v -> finish());
        stopBtn.setOnClickListener(v -> Toast.makeText(this, "Stop button pressed", Toast.LENGTH_SHORT).show());
    }

    private void initVars() {
        stopBtn = findViewById(R.id.stopbtn);
        sessionIdTv = findViewById(R.id.sessiontv);
        attendenceRecycler = findViewById(R.id.attendance_recyclerview);
        backBtn = toolbar.findViewById(R.id.backBtn);

    }
}