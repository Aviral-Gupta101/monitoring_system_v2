package com.aviralgupta.site.monitoring_system.config;

import com.aviralgupta.site.monitoring_system.service.monitors.AbstractMonitor;
import com.aviralgupta.site.monitoring_system.socket.SocketIoProperties;
import com.corundumstudio.socketio.SocketIOServer;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class MyApplicationConfig {

    private final SocketIoProperties socketIoProperties;

    public MyApplicationConfig(SocketIoProperties socketIoProperties) {
        this.socketIoProperties = socketIoProperties;
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public List<AbstractMonitor> getAllMonitors(){
        return new ArrayList<>();
    }

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(socketIoProperties.getHostname());
        config.setPort(socketIoProperties.getPort());
        config.setOrigin("*");
        config.setPingInterval(25000); // 25 seconds between pings
        config.setPingTimeout(60000);  // 60 seconds timeout
        return new SocketIOServer(config);
    }
}
