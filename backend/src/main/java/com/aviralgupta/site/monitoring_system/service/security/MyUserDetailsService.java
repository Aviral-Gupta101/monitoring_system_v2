package com.aviralgupta.site.monitoring_system.service.security;

import com.aviralgupta.site.monitoring_system.entity.User;
import com.aviralgupta.site.monitoring_system.repo.UserRepo;
import com.aviralgupta.site.monitoring_system.util.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> optionalUser = userRepo.findByEmail(email);

        if(optionalUser.isEmpty())
            throw new UsernameNotFoundException("User not found");

        User user = optionalUser.get();

        return new UserPrincipal(user.getEmail(), user.getPassword());

    }
}
