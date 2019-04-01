package com.csquared.mvserver.data;

import com.google.gson.annotations.SerializedName;

import java.net.Socket;

public class User {

    private transient Socket socket;

    @SerializedName("nickname")
    private String nickname;

    @SerializedName("auth")
    private int auth;

    public User(Socket socket, String nickname, int auth) {
        this.socket = socket;
        this.nickname = nickname;
        this.auth = auth;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getAuth() {
        return auth;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }

    public String getAuthString() {
        if (auth == C.AUTH_NONE) {
            return "无权限";
        } else if (auth == C.AUTH_COMMON) {
            return "一般";
        } else if (auth == C.AUTH_FULL) {
            return "高级";
        } else if (auth == C.AUTH_ADMIN){
            return "管理员";
        } else {
            return "无权限";
        }
    }

}
