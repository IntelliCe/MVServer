package com.csquared.mvserver.data;

import com.google.gson.annotations.SerializedName;

public class VotingState {

    @SerializedName("phase")
    private int phase;

    @SerializedName("nickname")
    private String nickname;

    @SerializedName("counter")
    private int counter;

    public VotingState(int phase, String nickname, int counter) {
        this.phase = phase;
        this.nickname = nickname;
        this.counter = counter;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getPhase() {
        return phase;
    }

    public String getNickname() {
        return nickname;
    }

    public int getCounter() {
        return counter;
    }

}
