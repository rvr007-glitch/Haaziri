package com.hackslash.haaziri.models;

/**
 * Session model class to handle team sessions
 */
public class Session {
    private String sessionName;
    private long timeStamp;

    public Session() {
    }

    public Session(String sessionName, long timeStamp) {
        this.sessionName = sessionName;
        this.timeStamp = timeStamp;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
