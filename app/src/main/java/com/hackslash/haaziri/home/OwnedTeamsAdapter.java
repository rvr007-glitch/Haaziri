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

public class OwnedTeamsAdapter extends RecyclerView.Adapter<OwnedTeamsAdapter.ViewHolder> {
    private List<Team> ownedTeamList;

    public void Adapter (List<Team>ownedTeamList){ this.ownedTeamList=ownedTeamList;}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View ownedTeamView = LayoutInflater.from(parent.getContext()).inflate(R.layout.owned_team_card,parent,false);
        return new ViewHolder(ownedTeamView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String ownedIcon = ownedTeamList.get(position).getTeamCode();

        holder.setData(ownedIcon);

    }

    @Override
    public int getItemCount() {
        return ownedTeamList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView ownedTeamsIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ownedTeamsIcon = itemView.findViewById(R.id.ownedTeamIcon);
        }

        public void setData(String ownedIcon) {
            ownedTeamsIcon.setText(ownedIcon);
        }
    }
}
