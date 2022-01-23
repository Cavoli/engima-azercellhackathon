package com.example.demo.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final CurrentUser currentUser;

    public CustomAuthenticationToken(Object principal, Object credentials, CurrentUser currentUser) {
        super(principal, credentials);
        this.currentUser = currentUser;
    }

    public CustomAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, CurrentUser currentUser) {
        super(principal, credentials, authorities);
        this.currentUser = currentUser;
    }

    public CurrentUser getCurrentUser() {
        return currentUser;
    }
}
