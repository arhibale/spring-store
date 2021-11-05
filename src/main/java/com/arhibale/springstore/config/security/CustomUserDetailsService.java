package com.arhibale.springstore.config.security;

import com.arhibale.springstore.entity.PersonEntity;
import com.arhibale.springstore.service.PersonService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final PersonService personService;

    public CustomUserDetailsService(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        PersonEntity person = personService.findByLogin(login);
        return new CustomUserDetails(person);
    }
}
