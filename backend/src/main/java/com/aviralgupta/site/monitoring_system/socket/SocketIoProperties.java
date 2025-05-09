package com.aviralgupta.site.monitoring_system.socket;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "socketio")
@Getter
@Setter
public class SocketIoProperties {

    private String hostname;
    private int port;
}
