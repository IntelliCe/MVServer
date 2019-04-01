package com.csquared.mvserver.data;

import com.google.gson.annotations.SerializedName;

public class ClientTick {

    @SerializedName("type")
    private int type;

    @SerializedName("message")
    private String message;

    @SerializedName("nickname")
    private String nickname;

    @SerializedName("vote")
    private int vote;

    public ClientTick(int type) {
        this.type = type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public int getVote() {
        return vote;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
