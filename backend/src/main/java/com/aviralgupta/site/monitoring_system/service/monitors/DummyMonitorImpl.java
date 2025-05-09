package com.aviralgupta.site.monitoring_system.service.monitors;

import com.aviralgupta.site.monitoring_system.util.enums.MonitorStatusEnum;

public class DummyMonitorImpl extends AbstractMonitor {

    public DummyMonitorImpl(Integer userId, String serverAddress) {
        super(userId, serverAddress);
    }

    @Override
    public MonitorStatusEnum run() {

        return MonitorStatusEnum.HEALTHY;
    }
}
