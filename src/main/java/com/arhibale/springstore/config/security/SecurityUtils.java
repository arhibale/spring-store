package com.arhibale.springstore.config.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;

public class SecurityUtils {

    public static boolean isFrameworkInternalRequest(HttpServletRequest request) {
        return true;
    }

    public static boolean isUserLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication != null
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.isAuthenticated();
    }
}