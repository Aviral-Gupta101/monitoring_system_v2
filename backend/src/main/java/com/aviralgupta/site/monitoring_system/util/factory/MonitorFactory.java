package com.aviralgupta.site.monitoring_system.util.factory;

import com.aviralgupta.site.monitoring_system.service.monitors.AbstractMonitor;
import com.aviralgupta.site.monitoring_system.service.monitors.HttpCheckMonitorImpl;
import com.aviralgupta.site.monitoring_system.service.monitors.PortCheckMonitorImpl;
import com.aviralgupta.site.monitoring_system.util.enums.MonitorTypeEnum;

public class MonitorFactory {

    public static AbstractMonitor getMonitor(Integer userId, String serverAddress, MonitorTypeEnum type) {

        if(type == MonitorTypeEnum.PORT_CHECK)
            return new PortCheckMonitorImpl(userId, serverAddress);

        else if(type == MonitorTypeEnum.HTTP_CHECK)
            return new HttpCheckMonitorImpl(userId, serverAddress);

        throw new RuntimeException("Monitor type not defined, in factory method");
    }

    public static AbstractMonitor getMonitor(String monitorId, Integer userId, String serverAddress, MonitorTypeEnum type) {

        if(type == MonitorTypeEnum.PORT_CHECK)
            return new PortCheckMonitorImpl(monitorId, userId, serverAddress);

        else if(type == MonitorTypeEnum.HTTP_CHECK)
            return new HttpCheckMonitorImpl(monitorId, userId, serverAddress);

        throw new RuntimeException("Monitor type not defined, in factory method");
    }
}
