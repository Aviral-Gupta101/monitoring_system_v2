package com.aviralgupta.site.monitoring_system.controller;

import com.aviralgupta.site.monitoring_system.dto.MonitorDto;
import com.aviralgupta.site.monitoring_system.entity.Monitor;
import com.aviralgupta.site.monitoring_system.service.controller.UserService;
import com.aviralgupta.site.monitoring_system.util.RequestValidatorUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/monitors")
    public ResponseEntity<?> getAllMonitor(){

        List<Monitor> allMonitor = userService.getAllMonitor();
        return ResponseEntity.ok(allMonitor);
    }

    @PostMapping("/monitor/create")
    public ResponseEntity<?> createMonitor(@Valid @RequestBody MonitorDto monitorDto, BindingResult result){

        if(result.hasErrors())
            return RequestValidatorUtil.getErrors(result);

        Monitor createdMonitor = userService.createMonitor(monitorDto);
        return ResponseEntity.ok(createdMonitor);
    }

    @DeleteMapping("/monitor/delete/{id}")
    public ResponseEntity<?> createMonitor(@PathVariable String id){
        userService.deleteMonitor(id);
        return ResponseEntity.ok(Map.of("message", "Monitor Deleted"));
    }

    @PatchMapping("/monitor/enable/{id}")
    public ResponseEntity<?> enableMonitor(@PathVariable String id){
        userService.enableMonitor(id);
        return ResponseEntity.ok(Map.of("message", "Monitor enabled"));
    }

    @PatchMapping("/monitor/disable/{id}")
    public ResponseEntity<?> disableMonitor(@PathVariable String id){
        userService.disableMonitor(id);
        return ResponseEntity.ok(Map.of("message", "Monitor disabled"));
    }

}
