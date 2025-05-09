package com.aviralgupta.site.monitoring_system.service.monitors;

import com.aviralgupta.site.monitoring_system.util.enums.MonitorStatusEnum;
import java.net.InetSocketAddress;
import java.net.Socket;


public class PortCheckMonitorImpl extends AbstractMonitor {

    int connectionTimeout = 5 * 1000; // 5 seconds

    public PortCheckMonitorImpl(Integer userId, String serverAddress) {
        super(userId, serverAddress);
    }

    public PortCheckMonitorImpl(String monitorId, Integer userId, String serverAddress) {
        super(monitorId, userId, serverAddress);
    }

    @Override
    public MonitorStatusEnum run() {

        if(getPort() == null || getPort() == 0)
            throw new RuntimeException("Port not set");

        try {

            InetSocketAddress inetSocketAddress = new InetSocketAddress(getServerAddress(), getPort());
            Socket socket = new Socket();
            socket.connect(inetSocketAddress, connectionTimeout);
            socket.close();

            return MonitorStatusEnum.HEALTHY;

        } catch (Exception ex) {
            return MonitorStatusEnum.CRITICAL;
        }
    }
}
