package com.example.demo.websocket;

import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Map;

public class UserInterceptor extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(
            org.springframework.http.server.ServerHttpRequest request,
            org.springframework.web.socket.WebSocketHandler wsHandler,
            Map<String, Object> attributes) {

        if (request instanceof ServletServerHttpRequest servletRequest) {

            String query = servletRequest.getServletRequest().getQueryString();
            String username = "anonymous";

            if (query != null && query.contains("username=")) {
                username = query.split("username=")[1];
                username = URLDecoder.decode(username, StandardCharsets.UTF_8);
            }

            String sessionId = servletRequest.getServletRequest().getSession().getId();

            // 🔥 ADD USER HERE (NO EVENT LISTENER)
            OnlineUserStore.addUser(username, sessionId);

            System.out.println("🔥 Connected: " + username);

            final String finalUsername = username;

            return () -> finalUsername;
        }

        return super.determineUser(request, wsHandler, attributes);
    }
}