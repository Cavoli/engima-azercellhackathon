package com.example.demo.security;

import com.example.demo.model.EndUser;
import com.example.demo.repository.EndUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.security.AuthoritiesConstants.CUSTOMER;

@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final EndUserRepository endUserRepository;

    public DomainUserDetailsService(EndUserRepository endUserRepository) {
        this.endUserRepository = endUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        return endUserRepository.findById(endUserRepository.getEndUserByUsername(login).get().getId())
                .map(user -> createSpringSecurityUser(login, user))
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + login + " was not found in the database"));
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin, EndUser endUser) {

        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(new SimpleGrantedAuthority(CUSTOMER));
        return new org.springframework.security.core.userdetails.User(lowercaseLogin,
                endUser.getPassword(),
                grantedAuthorities);
    }
}
