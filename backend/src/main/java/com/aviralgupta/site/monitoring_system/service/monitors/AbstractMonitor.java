package com.aviralgupta.site.monitoring_system.service.monitors;

import com.aviralgupta.site.monitoring_system.entity.Monitor;
import com.aviralgupta.site.monitoring_system.exception.custom_exceptions.NotFoundException;
import com.aviralgupta.site.monitoring_system.repo.MonitorRepo;
import com.aviralgupta.site.monitoring_system.repo.UserRepo;
import com.aviralgupta.site.monitoring_system.socket.SocketService;
import com.aviralgupta.site.monitoring_system.util.SpringContextHolder;
import com.aviralgupta.site.monitoring_system.util.enums.MonitorStatusEnum;
import com.aviralgupta.site.monitoring_system.util.pojo.MonitorResult;
import com.aviralgupta.site.monitoring_system.util.security.UserPrincipal;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.*;

@Getter
public abstract class AbstractMonitor {

    protected static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);

    private final String id;
    private final Integer userId;
    private final String serverAddress; // (IPv4/domain)
    private final SocketService socketService = SpringContextHolder.getBean(SocketService.class);
    private final UserRepo userRepo = SpringContextHolder.getBean(UserRepo.class);
    private final MonitorRepo monitorRepo = SpringContextHolder.getBean(MonitorRepo.class);
    private ScheduledFuture<?> scheduledTask = null;
    private final String userEmail;

    @Setter
    private Boolean isScheduled = false; // becomes true when schedule method is called
    @Setter
    private Integer scheduledInterval; // seconds
    @Setter
    private Boolean isDisabled = false;
    @Setter
    private Integer port;

    public AbstractMonitor(Integer userId, String serverAddress) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.serverAddress = serverAddress;
        this.userEmail = userRepo.findById(userId).get().getEmail();
    }

    public AbstractMonitor(String monitorId, Integer userId, String serverAddress) {
        this.id = monitorId;
        this.userId = userId;
        this.serverAddress = serverAddress;
        this.userEmail = userRepo.findById(userId).get().getEmail();
    }

    public void scheduleOnce(){

        scheduledExecutorService.submit(() -> {
            MonitorStatusEnum status = run();
            notifyResult(status);
        });
    }

    public void schedule(){

        if(scheduledInterval < 5)
            throw new RuntimeException("Scheduled interval is less than 5");

        if(isScheduled)
            return;

        isScheduled = true;

        this.scheduledTask = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            MonitorStatusEnum status = run();
            notifyResult(status);
        }, 0, scheduledInterval, TimeUnit.SECONDS);
    }

    public void stopSchedule(){

        if(!isScheduled)
                return;

        isScheduled = false;

        if(scheduledTask != null)
            scheduledTask.cancel(true);
    }

    private void updateMonitorStatusInDB(MonitorStatusEnum status){
        Optional<Monitor> foundMonitor = monitorRepo.findById(id);

        if(foundMonitor.isEmpty())
            throw new NotFoundException("Monitor with id " + id + " not found");

        Monitor monitor = foundMonitor.get();
        monitor.setMonitorStatus(status);
        monitor.setLastNotifiedAt(LocalDateTime.now());

        monitorRepo.save(monitor);
    }

    public void notifyResult(MonitorStatusEnum status){

        updateMonitorStatusInDB(status);

        // TODO: REMOVE THE LOG STATEMENT
        MonitorResult result = new MonitorResult(id, userId, serverAddress, status);
        System.out.println(result);
        socketService.notifyClient(userEmail, result);
    }

    public abstract MonitorStatusEnum run();
}
