package com.csquared.mvserver.data;

import com.google.gson.annotations.SerializedName;

public class MapInfo {

    @SerializedName("states")
    private int[] states;

    public MapInfo() {
        states = new int[8];
        for (int i = C.MAP_DUST; i < C.MAP_COBBLE + 1; i++) {
            set(i, C.MAP_NOT_IN_MAP_POOL);
        }
    }

    public int get(int map) {
        return states[map - C.MAP_DUST];
    }

    public void set(int map, int state) {
        this.states[map - C.MAP_DUST] = state;
    }

    public void allInMapPool() {
        for (int i = C.MAP_DUST; i < C.MAP_COBBLE + 1; i++) {
            set(i, C.MAP_TBD);
        }
    }

    public void addVote(int map) {
        set(map, get(map) + 1);
    }

    public void ban(int map) {
        set(map, C.MAP_BANNED);
    }

    public void pick(int map) {
        set(map, C.MAP_PICKED);
    }

    public int getLeft() {
        int left = 8;
        for (int i = C.MAP_DUST; i < C.MAP_COBBLE + 1; i++) {
            if (get(i) == C.MAP_BANNED) {
                left--;
            }
        }
        return left;
    }

}
