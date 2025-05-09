package com.aviralgupta.site.monitoring_system.socket;

import com.aviralgupta.site.monitoring_system.util.pojo.MonitorResult;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.stereotype.Service;

@Service
public class SocketService {

    private final SocketIOServer socketIOServer;

    public SocketService(SocketIOServer socketIOServer) {
        this.socketIOServer = socketIOServer;
    }

    public void notifyClient(String room, MonitorResult result) {

        System.out.println("Inside notifyClient");
        socketIOServer.getRoomOperations(room).sendEvent("status", result);
        System.out.println("Sending event to room " + room);
    }
}
