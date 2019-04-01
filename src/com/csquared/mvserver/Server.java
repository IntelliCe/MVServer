package com.csquared.mvserver;

import com.csquared.mvserver.data.*;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {

    private ServerSocket server;
    private Socket socket;

    private int status;
    private String serverName = "Test Server";
    private int serverVoteType = C.VOTE_TYPE_BAN;
    private int serverAuthLevel = C.AUTH_COMMON;
    private int serverAuthAmount = 0;
    private int serverMapLeft = 1;
    private MapInfo mapInfo = new MapInfo();

    private CountingThread countingThread = new CountingThread();
    private int votingWaitTime = 10;

    private static final List<Socket> clients = new CopyOnWriteArrayList<>();
    private static final List<User> users = new CopyOnWriteArrayList<>();

    public void start() {

        status = C.STATUS_AWAITING;
        try {

            server = new ServerSocket(27010);
            new HeartbeatThread().start();
            System.out.println("Server started on port 27010.");

            countingThread.start();

            while (true) {
                socket = server.accept();
                clients.add(socket);
                users.add(
                        new User(socket,
                                socket.getInetAddress().toString(),
                                C.AUTH_COMMON));
                ServerThread thread = new ServerThread(socket);
                thread.start();
                userChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * HANDLER
     */
    private void handleClientTick(Socket socket, ClientTick tick) {

        int tickType = tick.getType();

        switch (tickType) {

            case C.TYPE_MESSAGE:
                String message = tick.getMessage();
                String nickname = getUserNickname(socket);
                String text = String.format("[%s] %s", nickname, message);
                ServerTick serverTick = new ServerTick(C.TYPE_MESSAGE);
                serverTick.setMessage(text);
                sendBroadcast(new Gson().toJson(serverTick, ServerTick.class));
                break;

            case C.TYPE_SET_NICKNAME:
                String newNickname = tick.getNickname();
                for (User user : users) {
                    if (user.getSocket().equals(socket)) {
                        user.setNickname(newNickname);
                        userChanged();
                    } else {
                        System.out.println("[?] Nickname from unknown source.");
                    }
                }
                break;

            case C.TYPE_VOTE:
                break;

        }

    }

    /**
     * UTILS
     */

    private void userChanged() {

        ServerTick tick = new ServerTick(C.TYPE_USERS_CHANGED);
        tick.setUsers(users);
        sendBroadcast(new Gson().toJson(tick, ServerTick.class));

    }

    private String getUserNickname(Socket socket) {

        for (User user : users) {
            if (user.getSocket().equals(socket)) {
                return user.getNickname();
            }
        }
        return "null";

    }

    private void userDisconnected(Socket socket) {

        for (int i = 0; i < clients.size(); i++) {
            if (socket.equals(clients.get(i))) {
                synchronized (clients) {
                    clients.remove(i);
                }
                synchronized (users) {
                    users.remove(i);
                }
            }
            userChanged();
        }

    }

    private void serverInfoChanged() {

        ServerInfo serverInfo = new ServerInfo(serverName, serverVoteType, serverMapLeft, serverAuthLevel, serverAuthAmount);
        ServerTick serverTick = new ServerTick(C.TYPE_SERVER_INFO_CHANGED);
        serverTick.setServerInfo(serverInfo);
        sendBroadcast(new Gson().toJson(serverTick, ServerTick.class));

    }

    void say(String s) {

        String text = String.format("[Console] %s", s);
        ServerTick serverTick = new ServerTick(C.TYPE_MESSAGE);
        serverTick.setMessage(text);
        sendBroadcast(new Gson().toJson(serverTick, ServerTick.class));

    }

    /**
     * VOTING RELATED
     */

    private int voterIndex = -1;
    int phase;

    void bpStart() {

        switch (serverVoteType) {

            case C.VOTE_TYPE_BAN:
                break;

            default:
                return;

        }

        ServerTick tick = new ServerTick(C.TYPE_SERVER_INFO_CHANGED);
        ServerInfo info = new ServerInfo(serverName, serverVoteType, serverMapLeft, serverAuthLevel, serverAuthAmount);
        info.setStatus(C.STATUS_ONGOING);
        tick.setServerInfo(info);
        sendBroadcast(new Gson().toJson(tick, ServerTick.class));
        status = C.STATUS_ONGOING;
        voterIndex = 0;
        phase = C.PHASE_READY;
        countingThread.setCount(6);
        countingThread.go();

    }

    private void toNextVoterByTurn(boolean isLoop) {

        for (int i = voterIndex; i < users.size(); i++) {
            if (users.get(i).getAuth() == serverAuthLevel) {
                voterIndex = i;
                return;
            } else if (i == users.size() - 1 && isLoop) {
                i = 0;
            } else {
                voterIndex = -1;
                return;
            }
        }

    }

    void bpStop() {

        ServerTick tick = new ServerTick(C.TYPE_SERVER_INFO_CHANGED);
        ServerInfo info = new ServerInfo(serverName, serverVoteType, serverMapLeft, serverAuthLevel, serverAuthAmount);
        info.setStatus(C.STATUS_AWAITING);
        tick.setServerInfo(info);
        sendBroadcast(new Gson().toJson(tick, ServerTick.class));
        status = C.STATUS_AWAITING;

    }

    private void onCountingDown() {

        ServerTick tick = new ServerTick(C.TYPE_VOTING_STATE_CHANGED);
        VotingState state = new VotingState(phase, "", countingThread.getCount());

        if (phase == C.PHASE_VOTING) {
            state.setNickname(users.get(voterIndex).getNickname());
        }

        tick.setVotingState(state);
        sendBroadcast(new Gson().toJson(tick, ServerTick.class));

    }

    private void onCountingReset() {


        ServerTick tick = new ServerTick(C.TYPE_VOTING_STATE_CHANGED);


    }

    private void onCountingFinished() {

        if (phase == C.PHASE_READY) {
            toNextVoterByTurn(false);
            phase = C.PHASE_VOTING;
            countingThread.setCount(votingWaitTime);
        } else if (phase == C.PHASE_VOTING) {
            onCountingReset();
        }

    }

    /**
     * GET & SET METHODS
     */

    public List<User> getUserList() {
        return users;
    }

    public void setServerName(String s) {
        serverName = s;
        serverInfoChanged();
    }

    public void setServerVoteType(int type) {
        serverVoteType = type;
        serverInfoChanged();
    }

    /**
     * DATA TRANSFERRING PROTOCOLS
     */

    private void sendData(Socket socket, String data) {

        PrintWriter writer;

        try {
            System.out.println("[>] Send: " + socket.getInetAddress() + ": " + data);
            writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(data);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendBroadcast(String data) {

        for (int i = clients.size() - 1; i >= 0; i--) {
            try {
                System.out.println("[>] Broadcast: " + data);
                PrintWriter writer = new PrintWriter(clients.get(i).getOutputStream(), true);
                writer.println(data);
                writer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Inner class: HeartbeatThread
     */
    class HeartbeatThread extends Thread {

        @Override
        public void run() {

            while (true) {
                try {
                    System.out.println("[♥] Heartbeat package delivered to clients.");
                    ServerTick tick = new ServerTick(C.TYPE_HEARTBEAT);
                    String data = new Gson().toJson(tick, ServerTick.class);
                    sendBroadcast(data);
                    sleep(30000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }

    }

    /**
     * Inner class: ServerThread
     */
    class ServerThread extends Thread {

        Socket socket;
        BufferedReader reader;

        private ServerThread(Socket socket) {

            this.socket = socket;

        }

        @Override
        public void run() {

            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                ServerInfo serverInfo = new ServerInfo(serverName, serverVoteType, serverMapLeft, serverAuthLevel, serverAuthAmount);
                ServerTick serverTick = new ServerTick(C.TYPE_SERVER_INFO_CHANGED);
                serverTick.setServerInfo(serverInfo);
                sendData(socket, new Gson().toJson(serverTick, ServerTick.class));

                try {
                    String data;
                    while ((data = reader.readLine()) != null) {
                        ClientTick tick = new Gson().fromJson(data, ClientTick.class);
                        handleClientTick(socket, tick);
                    }
                } catch (Exception e) {
                    System.out.println("[X] Lost connection from " + socket.getInetAddress());
                    userDisconnected(socket);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * Inner class: CountingThread
     */
    class CountingThread extends Thread {

        private boolean stopped;
        private int count;

        public CountingThread() {
            stopped = true;
            count = 5;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    if (!stopped) {
                        count--;
                        if (count < 0) {
                            onCountingFinished();
                        } else {
                            onCountingDown();
                        }
                    }
                    sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        void go() {
            stopped = false;
        }

        void shut() {
            stopped = true;
        }

        void setCount(int count) {
            this.count = count;
        }

        void reset(int count) {
            this.count = count;
            onCountingReset();
        }

        int getCount() {
            return count;
        }

    }

}