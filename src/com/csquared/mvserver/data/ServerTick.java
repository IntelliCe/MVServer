package com.csquared.mvserver.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServerTick {

    @SerializedName("type")
    private int type;

    @SerializedName("message")
    private String message;

    @SerializedName("serverInfo")
    private ServerInfo serverInfo;

    @SerializedName("users")
    private List<User> users;

    @SerializedName("mapInfo")
    private MapInfo mapInfo;

    @SerializedName("votingState")
    private VotingState votingState;

    @SerializedName("turn")
    private boolean turn;

    public ServerTick(int type) {
        this.type = type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setServerInfo(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public ServerInfo getserverInfo() {
        return serverInfo;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public MapInfo getMapInfo() {
        return mapInfo;
    }

    public void setMapInfo(MapInfo mapInfo) {
        this.mapInfo = mapInfo;
    }

    public VotingState getVotingState() {
        return votingState;
    }

    public void setVotingState(VotingState votingState) {
        this.votingState = votingState;
    }

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

}
