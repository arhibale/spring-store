package com.arhibale.springstore.config.security;

import com.arhibale.springstore.entity.PersonEntity;
import com.arhibale.springstore.util.DecodeJwtToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final PersonEntity person;
    private final List<GrantedAuthority> authorities;

    public CustomUserDetails(PersonEntity person) {
        this.person = person;
        this.authorities = DecodeJwtToken.getRoles();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return person.getPassword();
    }

    @Override
    public String getUsername() {
        return person.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        int exp = (int) DecodeJwtToken.decodeByKey("exp");
        long now = Instant.now().toEpochMilli() / 1000;
        return exp > now;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public PersonEntity getPerson() {
        return person;
    }
}
