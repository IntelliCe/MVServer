package com.csquared.mvserver.data;

import com.google.gson.annotations.SerializedName;

public class ServerInfo {

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private int type;

    @SerializedName("auth")
    private int auth;

    @SerializedName("amount")
    private int amount;

    @SerializedName("left")
    private int left;

    @SerializedName("status")
    private int status;

    public ServerInfo(String name, int type, int left, int auth, int amount) {
        this.name = name;
        this.type = type;
        this.auth = auth;
        this.left = left;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public int getAuth() {
        return auth;
    }

    public int getAmount() {
        return amount;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public int getLeft() {
        return left;
    }

}
