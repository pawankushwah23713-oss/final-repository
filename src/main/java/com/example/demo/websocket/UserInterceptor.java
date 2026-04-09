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

            // query string: username=pawan%40gmail.com
            String query = servletRequest.getServletRequest().getQueryString();
            String username = "anonymous";

            if (query != null && query.contains("username=")) {
                // simple parsing
                username = query.split("username=")[1];
                // decode URL encoded value
                username = URLDecoder.decode(username, StandardCharsets.UTF_8);
            }

            System.out.println("🔥 Connected user: " + username);

            final String finalUsername = username;
            return new Principal() {
                @Override
                public String getName() {
                    return finalUsername;
                }
            };
        }

        return super.determineUser(request, wsHandler, attributes);
    }
}