package com.labtech.backend.util;

import com.labtech.backend.constants.EncurtadorConstants;
import com.labtech.backend.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class ApplicationUtility {

    public static  String getLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
        authentication.getPrincipal().equals("anonymousUser")){
            return EncurtadorConstants.SYSTEM;
        }
        Object principal = authentication.getPrincipal();
        String email;
        if (principal instanceof User user){
            email = user.getEmail();
        } else {
            email = principal.toString();
        }
        return email;
    }
}
