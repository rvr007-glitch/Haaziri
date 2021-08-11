package com.hackslash.haaziri.teamhome;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hackslash.haaziri.models.Session;

import java.util.ArrayList;

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

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecentSessionsAdapter.RecentHolder holder, int position) {
        //TODO: Implement function as per requirement
    }

    @Override
    public int getItemCount() {
        return recentSessions.size();
    }

    public class RecentHolder extends RecyclerView.ViewHolder {
        //TODO: Write code for the viewholder as per requirement as per requirement
        public RecentHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
