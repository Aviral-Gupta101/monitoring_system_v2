package com.aviralgupta.site.monitoring_system.util;

import com.aviralgupta.site.monitoring_system.util.security.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

public class GetPrincipalUser {

    public static String getCurrentUserEmail(){
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal == null)
            throw new RuntimeException("Unable to get current user email, as principle is null");

        return principal.getUsername();
    }
}
