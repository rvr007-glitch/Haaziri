package com.hackslash.haaziri.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hackslash.haaziri.R;
import com.hackslash.haaziri.models.Team;

import java.util.List;

public class JoinedTeamsAdapter extends RecyclerView.Adapter<JoinedTeamsAdapter.ViewHolder> {
    private List<Team> joinTeamList;

    public JoinedTeamsAdapter(List<Team>joinTeamList){ this.joinTeamList=joinTeamList; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View joinView = LayoutInflater.from(parent.getContext()).inflate(R.layout.joined_team_card,parent,false);
        return new ViewHolder(joinView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
           String joinTeamIcon = joinTeamList.get(position).getTeamCode();
           String joinTeamName = joinTeamList.get(position).getTeamName();

           holder.setData(joinTeamIcon,joinTeamName);
    }

    @Override
    public int getItemCount() {
        return joinTeamList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView icon;
        private TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.joinedTeamIcon);
            name = itemView.findViewById(R.id.joinedTeamText);

        }

        public void setData(String joinTeamIcon, String joinTeamName) {
            icon.setText(joinTeamIcon);
            name.setText(joinTeamName);

        }
    }
}
