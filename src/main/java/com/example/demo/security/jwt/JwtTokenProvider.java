package com.example.demo.security.jwt;

import com.example.demo.model.EndUser;
import com.example.demo.repository.EndUserRepository;
import com.example.demo.security.CurrentUser;
import com.example.demo.security.CustomAuthenticationToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {

    private static final String BASE64_KEY = "ZGVsaXZlcnlvbnRoZWdvZnJvbWNyYm50b2Jha3V0b3dvcmxkemFydHp1cnRjYXJ0MzM0MzRlZGY0ZjRmODRoZjk4NGhmOTRoZmg0Zmg4ZmY0NGYK";

    private static final String AUTHORITIES_KEY = "auth";

    private static final String USER_DETAILS_KEY = "userDetails";

    private Key key;

    private long tokenValidityInMilliseconds;

    private long tokenValidityInMillisecondsForRememberMe;

    private final EndUserRepository endUserRepository;

    @Autowired
    public JwtTokenProvider(EndUserRepository endUserRepository) {
        this.endUserRepository = endUserRepository;
    }

    @PostConstruct
    public void init() {
        byte[] keyBytes;
        keyBytes = Decoders.BASE64.decode(BASE64_KEY);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.tokenValidityInMilliseconds = 1000 * 60 * 24;
    }

    public String createToken(Authentication authentication, boolean rememberMe, Long id) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        if (authorities.isEmpty()) {
            authorities = "CUSTOMER";
        }

        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }

        EndUser endUser = endUserRepository.findById(id).get();

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .claim(USER_DETAILS_KEY, this.getUserDetailsClaim(endUser))
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        User principal = new User(claims.getSubject(), "", authorities);
        CurrentUser currentUser = new CurrentUser();
        currentUser.setAuthorities(authorities);
        Object userObj = claims.get(USER_DETAILS_KEY);
        if (userObj != null) {
            LinkedHashMap userHashMap = (LinkedHashMap) userObj;
            currentUser.setId(Long.parseLong((String) userHashMap.get("userId")));
            currentUser.setFirstName((String) userHashMap.get("firstName"));
            currentUser.setLastName((String) userHashMap.get("lastName"));
        }
        return new CustomAuthenticationToken(principal, token, authorities, currentUser);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace.", e);
        }
        return false;
    }

    private HashMap<String, String> getUserDetailsClaim(EndUser endUser) {
        HashMap<String, String> userDetails = new HashMap<>();

        if (endUser != null) {
            userDetails.put("userId", endUser.getId().toString());
            userDetails.put("firstName", endUser.getFirstName());
            userDetails.put("lastName", endUser.getLastName());
        }
        return userDetails;
    }
}
