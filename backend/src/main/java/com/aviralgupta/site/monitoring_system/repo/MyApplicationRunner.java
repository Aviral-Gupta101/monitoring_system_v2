package com.aviralgupta.site.monitoring_system.repo;

import com.aviralgupta.site.monitoring_system.dto.MonitorDto;
import com.aviralgupta.site.monitoring_system.entity.Monitor;
import com.aviralgupta.site.monitoring_system.service.monitors.MonitorService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import java.util.List;


@Component
public class MyApplicationRunner implements ApplicationRunner {

    private final MonitorService monitorService;
    private final MonitorRepo monitorRepo;
    private final ModelMapper modelMapper;


    public MyApplicationRunner(MonitorService monitorService, MonitorRepo monitorRepo, ModelMapper modelMapper) {
        this.monitorService = monitorService;
        this.monitorRepo = monitorRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<Monitor> monitorList = monitorRepo.findAll();

        for (Monitor monitor : monitorList) {

            MonitorDto dto = modelMapper.map(monitor, MonitorDto.class);
            monitorService.createMonitor(monitor.getId(), monitor.getUser().getId(), dto);
        }
    }
}
