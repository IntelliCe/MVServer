package com.csquared.mvserver;

import com.csquared.mvserver.data.User;

import java.util.List;
import java.util.Scanner;

public class Main {

    final static Server server = new Server();

    public static void main(String[] args) {

        new Thread() {
            @Override
            public void run() {
                server.start();
            }
        }.start();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            cmd(scanner.nextLine());
        }

    }

    private static void cmd(String cmd) {

        if (!cmd.trim().equals("")) {
            String[] command = cmd.split(" ");
            String f = command[0];
            String[] args = new String[command.length - 1];
            for (int i = 1; i < command.length; i++) {
                args[i - 1] = command[i];
            }
            try {
                switch (f) {
                    case "version":
                        version();
                        break;
                    case "servername":
                        serverName(args);
                        break;
                    case "say":
                        say(args);
                        break;
                    case "clients":
                        clients();
                        break;
                    case "start":
                        server.bpStart();
                        break;
                    case "stop":
                        server.bpStop();
                        break;
                    case "quit":
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Unknown command: " + f);
                }
            } catch (Exception e) {
                System.out.println("[!] Command arguments error.");
            }
        }

    }

    private static void version() {
        System.out.println("** NJUPT MAJOR MapVoter Server 1.0 **");
    }

    private static void serverName(String[] s) {
        server.setServerName(getFullString(s));
    }

    private static void say(String[] s) {
        server.say(getFullString(s));
    }

    private static void clients() {
        List<User> users = server.getUserList();
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            System.out.printf("#%d, InetAddress=%s, Nickname=%s", i, user.getSocket().getInetAddress(), user.getNickname());
        }
    }

    private static String getFullString(String[] s) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < s.length; i++) {
            builder.append(s[i]);
            if (i < s.length - 1) {
                builder.append(" ");
            }
        }
        return builder.toString();
    }

}
