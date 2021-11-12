package com.arhibale.springstore.util;

import com.arhibale.springstore.config.security.CustomUserDetails;
import com.arhibale.springstore.entity.PersonEntity;
import org.springframework.security.core.context.SecurityContextHolder;


public class PersonUtil {
    public static PersonEntity getCurrentPerson() {
        var details = SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (details instanceof CustomUserDetails) {
            return ((CustomUserDetails) details).getPerson();
        }
        return null;
    }
}