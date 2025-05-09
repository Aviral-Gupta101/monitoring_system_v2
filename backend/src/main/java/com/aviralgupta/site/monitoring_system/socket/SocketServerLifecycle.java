package com.aviralgupta.site.monitoring_system.socket;

import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class SocketServerLifecycle {

    private final SocketIOServer server;

    public SocketServerLifecycle(SocketIOServer server) {
        this.server = server;
    }

    @PostConstruct
    private void startServer() {
        server.start();
    }

    @PreDestroy
    private void stopServer() {
        server.stop();
    }
}
