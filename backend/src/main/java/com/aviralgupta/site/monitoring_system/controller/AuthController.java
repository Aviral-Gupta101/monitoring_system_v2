package com.aviralgupta.site.monitoring_system.controller;

import com.aviralgupta.site.monitoring_system.dto.UserAuthDto;
import com.aviralgupta.site.monitoring_system.service.controller.AuthService;
import com.aviralgupta.site.monitoring_system.util.GetPrincipalUser;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUser(){
        String currentUserEmail = GetPrincipalUser.getCurrentUserEmail();
        return ResponseEntity.ok(Map.of("message", "user is authenticated : " + currentUserEmail));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserAuthDto userAuthDto){

        authService.signup(userAuthDto);
        return ResponseEntity.ok(Map.of("message", "User account created"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserAuthDto userAuthDto){

        String token = authService.login(userAuthDto);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
