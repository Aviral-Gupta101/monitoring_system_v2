package com.aviralgupta.site.monitoring_system.service.monitors;

import com.aviralgupta.site.monitoring_system.dto.MonitorDto;
import com.aviralgupta.site.monitoring_system.exception.custom_exceptions.MonitorException;
import com.aviralgupta.site.monitoring_system.util.enums.MonitorTypeEnum;
import com.aviralgupta.site.monitoring_system.util.factory.MonitorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MonitorService {

    private final List<AbstractMonitor> monitorList;

    @Autowired
    public MonitorService(List<AbstractMonitor> monitorList) {
        this.monitorList = monitorList;
    }

    public String createMonitor(int userId, MonitorDto dto){

        AbstractMonitor monitor = MonitorFactory.getMonitor(userId, dto.getServerAddress(), dto.getType());

        setupMonitor(dto, monitor);

        return monitor.getId();
    }

    public String createMonitor(String monitorId, int userId, MonitorDto dto){

        AbstractMonitor monitor = MonitorFactory.getMonitor(monitorId, userId, dto.getServerAddress(), dto.getType());

        setupMonitor(dto, monitor);

        return monitor.getId();
    }

    private void setupMonitor(MonitorDto dto, AbstractMonitor monitor) {

        if(dto.getType() == MonitorTypeEnum.PORT_CHECK)
            monitor.setPort(dto.getPort());

        else if(dto.getType() == MonitorTypeEnum.HTTP_CHECK){
            ((HttpCheckMonitorImpl) monitor).setHttps(dto.getHttps());
        }
        monitor.setScheduledInterval(dto.getScheduleInterval());
        monitor.schedule();
        monitorList.add(monitor);
    }

    public void deleteMonitor(String monitorId){

        try{
            disableMonitor(monitorId);
            monitorList.removeIf(item -> item.getId().equals(monitorId));

        } catch (MonitorException exception){
            throw new MonitorException("Unable to delete monitor : " + exception.getMessage());
        }
    }

    public void enableMonitor(String monitorId){

        AbstractMonitor monitor = getMonitorById(monitorId);

        if(monitor == null)
            throw new MonitorException("Unable to find monitor in system cache");

        monitor.schedule();
    }

    public void disableMonitor(String monitorId){

        AbstractMonitor monitor = getMonitorById(monitorId);

        if(monitor == null)
            throw new MonitorException("Unable to find monitor in system cache");

        monitor.stopSchedule();
    }

    private AbstractMonitor getMonitorById(String monitorId){

        for (AbstractMonitor monitor : monitorList) {
            if(monitor.getId().equals(monitorId))
                return monitor;
        }

        return null;
    }
}
