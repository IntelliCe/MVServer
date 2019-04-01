package com.csquared.mvserver.data;

@SuppressWarnings("all")
public class C {

    //
    public static final int TYPE_SERVER_INFO_CHANGED = 0x01;
    public static final int TYPE_USERS_CHANGED = 0x02;
    public static final int TYPE_MESSAGE = 0x03;
    public static final int TYPE_VOTE = 0x04;
    public static final int TYPE_HEARTBEAT = 0x05;
    public static final int TYPE_SET_NICKNAME = 0x06;
    public static final int TYPE_VOTING_STATE_CHANGED = 0x07;
    public static final int TYPE_TURN_CHANGE = 0x08;

    //
    public static final int MAP_DUST = 0x11;
    public static final int MAP_MIRAGE = 0x12;
    public static final int MAP_INFERNO = 0x13;
    public static final int MAP_OVERPASS = 0x14;
    public static final int MAP_TRAIN = 0x15;
    public static final int MAP_CACHE = 0x16;
    public static final int MAP_NUKE = 0x17;
    public static final int MAP_COBBLE = 0x18;

    public static final int MAP_BANNED = -1;
    public static final int MAP_PICKED = 999;
    public static final int MAP_NOT_IN_MAP_POOL = -2;
    public static final int MAP_TBD = 0;

    //
    public static final int AUTH_NONE = 0x21;
    public static final int AUTH_COMMON = 0x22;
    public static final int AUTH_FULL = 0x23;
    public static final int AUTH_ADMIN = 0x24;

    //
    public static final int VOTE_TYPE_STANDARD = 0x31;
    public static final int VOTE_TYPE_BAN = 0x32;
    public static final int VOTE_TYPE_PICK = 0x33;
    public static final int VOTE_TYPE_VOTE_TO_BAN = 0x34;
    public static final int VOTE_TYPE_VOTE_TO_PICK = 0x35;

    //
    public static final int STATUS_AWAITING = 0x41;
    public static final int STATUS_ONGOING = 0x42;
    public static final int STATUS_FINISHED = 0x43;

    //
    public static final int PHASE_READY = 0x51;
    public static final int PHASE_VOTING = 0x52;

}
