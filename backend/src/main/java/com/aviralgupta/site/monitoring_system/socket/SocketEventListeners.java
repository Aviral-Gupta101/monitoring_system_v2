package com.aviralgupta.site.monitoring_system.socket;

import com.aviralgupta.site.monitoring_system.util.GetPrincipalUser;
import com.aviralgupta.site.monitoring_system.util.JwtUtil;
import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class SocketEventListeners {

    private final SocketIOServer server;
    private final JwtUtil jwtUtil;

    public SocketEventListeners(SocketIOServer server, JwtUtil jwtUtil) {
        this.server = server;
        this.jwtUtil = jwtUtil;
    }


    @PostConstruct
    public void addListeners() {
        server.addConnectListener(client -> {

            String token = client.getHandshakeData().getSingleUrlParam("token");

            if (token == null || token.isEmpty()) {
                client.disconnect();
                return;
            }

            boolean result = jwtUtil.validateToken(token);

            if (!result) {
                client.disconnect();
                return;
            }

            client.joinRoom(jwtUtil.extractUsername(token));
            System.out.println("Client connected: " + client.getSessionId() + " room " +
                    jwtUtil.extractUsername(token));

        });

        server.addDisconnectListener(client -> {
            System.out.println("Client disconnected: " + client.getSessionId());
        });

        server.addEventListener("join", String.class, (client, data, ackSender) -> {
            System.out.println("Client sent join event");
        });
    }
}
