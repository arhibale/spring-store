package com.arhibale.springstore.util;

import com.arhibale.springstore.config.security.CustomUserDetails;
import com.arhibale.springstore.entity.PersonEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;


public class PersonUtil {
    public static PersonEntity getCurrentPerson() {
        var details = getDetails();
        if (details instanceof CustomUserDetails) {
            return ((CustomUserDetails) details).getPerson();
        }
        return null;
    }

    public static boolean isRole(String role) {
        var details = getDetails();
        var roleSimpleGrantedAuthority = new SimpleGrantedAuthority(role);

        if (details instanceof CustomUserDetails) {
            return ((CustomUserDetails) details).getAuthorities().contains(roleSimpleGrantedAuthority);
        }
        return false;
    }

    public static Object getDetails() {
        return SecurityContextHolder.getContext().getAuthentication().getDetails();
    }
}