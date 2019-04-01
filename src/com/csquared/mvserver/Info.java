package com.csquared.mvserver;

@SuppressWarnings("unused")
public class Info {

    private String server;
    private String type;
    private String auth;

    public Info() {
        this.server = "0.0.0.0 [Disconnected]";
        this.type = "未指定";
        this.auth = "未指定";
    }

    public Info(String server, String type, String auth) {
        this.server = server;
        this.type = type;
        this.auth = auth;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String toComponent() {
        return server + "\n\n" + type + "\n" + auth;
    }

}
