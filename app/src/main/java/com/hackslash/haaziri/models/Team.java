package com.hackslash.haaziri.models;

public class Team {
    String teamName;
    String teamCode;

    public Team(){}

    public Team(String teamName, String teamCode) {
        this.teamName = teamName;
        this.teamCode = teamCode;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }
}
