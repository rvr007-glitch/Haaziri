package com.hackslash.haaziri.teamhome;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hackslash.haaziri.R;
import com.hackslash.haaziri.models.Session;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class RecentSessionsAdapter extends RecyclerView.Adapter<RecentSessionsAdapter.RecentHolder> {

    private final Context mContext;
    //Arraylist that would hold recent session objects
    private ArrayList<Session> recentSessions;

    public RecentSessionsAdapter(ArrayList<Session> recentSessions, Context mContext) {
        this.recentSessions = recentSessions;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecentSessionsAdapter.RecentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //TODO: Implement function as per requirement
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recents_card, parent, false);
        return new RecentSessionsAdapter.RecentHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecentSessionsAdapter.RecentHolder holder, int position) {
        holder.setData(recentSessions.get(position));
        //TODO: Implement function as per requirement
    }

    @Override
    public int getItemCount() {
        return recentSessions.size();
    }

    public class RecentHolder extends RecyclerView.ViewHolder {
        private CardView recentCardview;
        private TextView timestamp;

        //TODO: Write code for the viewholder as per requirement as per requirement
        public RecentHolder(@NonNull View itemView) {
            super(itemView);
            recentCardview = itemView.findViewById(R.id.recentcard);
            timestamp = itemView.findViewById(R.id.sessionDateTv);
        }

        private String getDate(long timestamp) {
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(timestamp * 1000);
            String date = DateFormat.format("dd-MM-yyyy", cal).toString();
            return date;
        }

        private void setData(Session value) {

            timestamp.setText(getDate(value.getTimeStamp()));
        }
    }
    }
