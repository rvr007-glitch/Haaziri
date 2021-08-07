package com.hackslash.haaziri.sessions;

import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.hackslash.haaziri.home.JoinedTeamsAdapter;
import com.hackslash.haaziri.home.TeamClickInterface;
import com.hackslash.haaziri.models.SessionAttendee;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.hackslash.haaziri.R;
import com.hackslash.haaziri.firebase.FirebaseVars;
import com.hackslash.haaziri.models.Team;

import java.util.ArrayList;

public class AttendeeAdapter extends RecyclerView.Adapter<AttendeeAdapter.ViewHolder> {
    private static final String TAG="AttendeeAdapter";
    private final Context mContext;
    private String teamCode;
    private String sessionId;


    public AttendeeAdapter( Context mContext,String teamCode,String sessionId) {

        this.mContext = mContext;
        this.teamCode=teamCode;
        this.sessionId=sessionId;
    }

    @NonNull
    @Override
    public AttendeeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View joinView = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendee_card, parent, false);
        return new AttendeeAdapter.ViewHolder(joinView);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendeeAdapter.ViewHolder holder, int position) {

        //here we are calling the fetch method the for current position method to get the team details from firebase
        holder.fetchAttendeeData(teamCode,sessionId);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView userImg;
        private ImageView icon;
        private TextView attendeeName;
        private CardView attendeeCardview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImg = itemView.findViewById(R.id.userimg);
            icon = itemView.findViewById(R.id.ticktv);
            attendeeName = itemView.findViewById(R.id.attendeename);
            attendeeCardview = itemView.findViewById(R.id.attendeecardview);

        }

        public void fetchAttendeeData(String teamCode,String sessionId) {

            FirebaseVars.mRootRef.child("/teams/"+teamCode+ "/sessions/"+sessionId+"/attendees/" ).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //as we get the team object from firebase we populate the details in the UI,
                    if (snapshot.exists())
                        for(DataSnapshot snapshot1:snapshot.getChildren())
                        setData(snapshot1.getValue(SessionAttendee.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, error.getMessage());
                }
            });
        }

        private void setData(SessionAttendee value) {

            attendeeName.setText(value.getName());
        }

    }

}
