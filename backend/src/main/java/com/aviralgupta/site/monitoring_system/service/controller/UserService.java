package com.aviralgupta.site.monitoring_system.service.controller;

import com.aviralgupta.site.monitoring_system.dto.MonitorDto;
import com.aviralgupta.site.monitoring_system.entity.Monitor;
import com.aviralgupta.site.monitoring_system.entity.User;
import com.aviralgupta.site.monitoring_system.exception.custom_exceptions.NotFoundException;
import com.aviralgupta.site.monitoring_system.repo.MonitorRepo;
import com.aviralgupta.site.monitoring_system.repo.UserRepo;
import com.aviralgupta.site.monitoring_system.service.monitors.MonitorService;
import com.aviralgupta.site.monitoring_system.util.GetPrincipalUser;
import com.aviralgupta.site.monitoring_system.util.enums.MonitorStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final MonitorRepo monitorRepo;
    private final MonitorService monitorService;

    @Autowired
    public UserService(UserRepo userRepo, MonitorRepo monitorRepo, MonitorService monitorService) {
        this.userRepo = userRepo;
        this.monitorRepo = monitorRepo;
        this.monitorService = monitorService;
    }

    private User getCurrentUser() {
        String currentUserEmail = GetPrincipalUser.getCurrentUserEmail();
        Optional<User> optionalUser = userRepo.findByEmail(currentUserEmail);

        if(optionalUser.isEmpty()) {
            throw new NotFoundException("Principle email exists but not present in DB");
        }

        return optionalUser.get();
    }

    public List<Monitor> getAllMonitor(){

        User foundUser = getCurrentUser();
        return foundUser.getMonitors();
    }

    public Monitor createMonitor(MonitorDto dto){

        User foundUser = getCurrentUser();

        Monitor newMonitor = Monitor.builder()
                .user(foundUser)
                .name(dto.getName())
                .type(dto.getType())
                .monitorStatus(MonitorStatusEnum.UNKNOWN)
                .isHttps(dto.getHttps())
                .port(dto.getPort())
                .serverAddress(dto.getServerAddress())
                .ScheduleInterval(dto.getScheduleInterval())
                .status(true)
                .notificationStatus(true)
                .build();

        if(foundUser.getMonitors() == null){
            foundUser.setMonitors(new ArrayList<>());
        }

        foundUser.getMonitors().add(newMonitor);

        String monitorId = monitorService.createMonitor(foundUser.getId(), dto);
        newMonitor.setId(monitorId);

        return monitorRepo.save(newMonitor);
    }

    public void deleteMonitor(String monitorId){

        Optional<Monitor> optionalMonitor = monitorRepo.findById(monitorId);

        if(optionalMonitor.isEmpty())
            throw new NotFoundException("Invalid monitor ID");

        Monitor monitor = optionalMonitor.get();

        if(!monitor.getUser().getEmail().equals(getCurrentUser().getEmail()))
            throw new NotFoundException("Invalid monitor ID");

        monitorService.deleteMonitor(monitorId);

        monitorRepo.deleteById(monitorId);

    }

    public void disableMonitor(String monitorId){

        Optional<Monitor> optionalMonitor = monitorRepo.findById(monitorId);

        if(optionalMonitor.isEmpty())
            throw new NotFoundException("Invalid monitor ID");

        monitorService.disableMonitor(monitorId);

        Monitor monitor = optionalMonitor.get();
        monitor.setStatus(false);
        monitorRepo.save(monitor);
    }

    public void enableMonitor(String monitorId){

        Optional<Monitor> optionalMonitor = monitorRepo.findById(monitorId);

        if(optionalMonitor.isEmpty())
            throw new NotFoundException("Invalid monitor ID");

        monitorService.enableMonitor(monitorId);

        Monitor monitor = optionalMonitor.get();
        monitor.setStatus(true);
        monitorRepo.save(monitor);
    }
}
