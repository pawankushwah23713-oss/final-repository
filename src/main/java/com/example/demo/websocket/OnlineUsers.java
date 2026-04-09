package com.example.demo.websocket;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class OnlineUsers {
    public static Set<String> users = ConcurrentHashMap.newKeySet();
}