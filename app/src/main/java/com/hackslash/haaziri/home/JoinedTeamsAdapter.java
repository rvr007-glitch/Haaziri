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

public class JoinedTeamsAdapter extends RecyclerView.Adapter<JoinedTeamsAdapter.ViewHolder> {

    private static final String TAG = "JoinedTeamsAdapter";
    private final Context mContext;
    private final TeamClickInterface mListener;
    //this adapter class helps in showing the joined teams list for the logged in user
    //we get the team code of the teams that the user has joined from the calling activity and store it in the joinedTeamsIds list
    private ArrayList<String> joinedTeamsIds;

    public JoinedTeamsAdapter(ArrayList<String> joinedTeamsIds, Context mContext, TeamClickInterface mListener) {
        this.joinedTeamsIds = joinedTeamsIds;
        this.mContext = mContext;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View joinView = LayoutInflater.from(parent.getContext()).inflate(R.layout.joined_team_card, parent, false);
        return new ViewHolder(joinView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //here we are calling the fetch method the for current position method to get the team details from firebase
        holder.fetchTeamData(joinedTeamsIds.get(position));
    }

    @Override
    public int getItemCount() {
        return joinedTeamsIds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView icon;
        private TextView name;
        private CardView mainCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.joinedTeamIcon);
            name = itemView.findViewById(R.id.joinedTeamText);
            mainCard = itemView.findViewById(R.id.mainCard);

            setupListeners();

        }

        private void setupListeners() {
            mainCard.setOnClickListener(v -> {
                mListener.onTeamClicked(joinedTeamsIds.get(getAdapterPosition()));
            });
        }


        /**
         * This method helps in fetching the team object from the firebase database
         *
         * @param teamId this is the team id of the team whose data is to be fetched
         */
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

        private void setData(Team value) {
            icon.setText(value.getTeamName().substring(0, 2));
            name.setText(value.getTeamName());
        }
    }
}
