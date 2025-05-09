package com.aviralgupta.site.monitoring_system.service.controller;

import com.aviralgupta.site.monitoring_system.dto.UserAuthDto;
import com.aviralgupta.site.monitoring_system.entity.User;
import com.aviralgupta.site.monitoring_system.exception.custom_exceptions.UserAlreadyExistsException;
import com.aviralgupta.site.monitoring_system.repo.UserRepo;
import com.aviralgupta.site.monitoring_system.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepo userRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepo userRepo, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public void signup(UserAuthDto userAuthDto) {

        System.out.println("Inside auth service");

        String email = userAuthDto.getEmail();
        String password = userAuthDto.getPassword();

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(7);

        Optional<User> foundUserOptional = userRepo.findByEmail(email);

        if (foundUserOptional.isPresent())
            throw new UserAlreadyExistsException("User account already exists");

        String hashedPassword = passwordEncoder.encode(password);
        User newUser = new User(email, hashedPassword);

        userRepo.save(newUser);
    }

    public String login(UserAuthDto dto) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

            if (!authentication.isAuthenticated())
                throw new UsernameNotFoundException("Invalid username or password");

            return jwtUtil.generateToken(dto.getEmail());

        } catch (Exception e){

            System.out.println("Got the exception: " + e.getMessage());
            throw e;
        }

    }

}
