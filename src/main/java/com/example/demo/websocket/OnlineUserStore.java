package com.example.demo.websocket;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class OnlineUserStore {

    public static final ConcurrentHashMap<String, Set<String>> userSessions = new ConcurrentHashMap<>();

    public static String normalize(String username) {
        return username == null ? null : username.trim().toLowerCase();
    }

    public static void addUser(String username, String sessionId) {
        username = normalize(username);

        userSessions
                .computeIfAbsent(username, k -> ConcurrentHashMap.newKeySet())
                .add(sessionId);

        System.out.println("🟢 Online: " + username + " | session: " + sessionId);
    }

    public static void removeUser(String username, String sessionId) {
        username = normalize(username);

        if (userSessions.containsKey(username)) {
            userSessions.get(username).remove(sessionId);

            if (userSessions.get(username).isEmpty()) {
                userSessions.remove(username);
                System.out.println("🔴 Offline: " + username);
            }
        }
    }

    public static boolean isOnline(String username) {
        username = normalize(username);

        System.out.println("Checking: " + username);
        System.out.println("Online Users: " + userSessions.keySet());

        return userSessions.containsKey(username)
                && !userSessions.get(username).isEmpty();
    }
}