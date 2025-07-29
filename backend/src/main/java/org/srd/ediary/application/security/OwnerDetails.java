package org.srd.ediary.application.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class OwnerDetails implements UserDetails {
    Long id;
    Collection<? extends GrantedAuthority> authorities;
    String password;
    String username;

    public OwnerDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, Long id) {
        this.id = id;
        this.authorities = authorities;
        this.password = password;
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }
}
