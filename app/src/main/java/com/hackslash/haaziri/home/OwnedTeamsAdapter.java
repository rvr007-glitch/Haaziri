package com.hackslash.haaziri.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.hackslash.haaziri.R;
import com.hackslash.haaziri.firebase.FirebaseVars;
import com.hackslash.haaziri.models.Team;

import java.util.ArrayList;
import java.util.List;

public class OwnedTeamsAdapter extends RecyclerView.Adapter<OwnedTeamsAdapter.ViewHolder> {
    private static final String TAG = "OwnedTeamsAdapter";
    private final Context mContext;
    private final TeamClickInterface mListener;
    private ArrayList<String> ownedTeamsIds;

    public OwnedTeamsAdapter(ArrayList<String> ownedTeamsIds, Context mContext, TeamClickInterface mListener) {
        this.ownedTeamsIds = ownedTeamsIds;
        this.mContext = mContext;
        this.mListener = mListener;
    }
    //this adapter class helps in showing the joined teams list for the logged in user
    //we get the team code of the teams that the user has joined from the calling activity and store it in the joinedTeamsIds list


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View ownedTeamView = LayoutInflater.from(parent.getContext()).inflate(R.layout.owned_team_card, parent, false);
        return new ViewHolder(ownedTeamView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.fetchTeamData(ownedTeamsIds.get(position));
    }

    @Override
    public int getItemCount() {
        return ownedTeamsIds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView ownedTeamsIcon;
        private CardView mainCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ownedTeamsIcon = itemView.findViewById(R.id.ownedTeamIcon);
            mainCard = itemView.findViewById(R.id.mainCard);

            setupListeners();
        }

        private void setupListeners() {
            mainCard.setOnClickListener(v -> {
                mListener.onTeamClicked(ownedTeamsIds.get(getAdapterPosition()));
            });
        }

        public void fetchTeamData(String teamId) {
            FirebaseVars.mRootRef.child("/teams/" + teamId + "/").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //as we get the team object from firebase we populate the details in the UI,
                    if (snapshot.exists())
                        setData(snapshot.getValue(Team.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, error.getMessage());
                }
            });
        }

        public void setData(Team ownedTeam) {
            ownedTeamsIcon.setText(ownedTeam.getTeamName().substring(0, 2));
        }
    }
}
