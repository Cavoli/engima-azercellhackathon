package com.example.demo.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


@Data
public class CurrentUser {

    private Long id;
    private String firstName;
    private String lastName;
    private Collection<? extends GrantedAuthority> authorities;

}
