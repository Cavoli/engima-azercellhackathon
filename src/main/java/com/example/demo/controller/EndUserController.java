package com.example.demo.controller;

import com.example.demo.security.jwt.JWTFilter;
import com.example.demo.security.jwt.JWTToken;
import com.example.demo.security.jwt.JwtTokenProvider;
import com.example.demo.service.dto.LoginDto;
import com.example.demo.service.dto.SignUpDto;
import com.example.demo.service.impl.EndUserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class EndUserController {
    private final EndUserService endUserService;

    public EndUserController(EndUserService endUserService) {
      this.endUserService = endUserService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> Signup(@Valid @RequestBody SignUpDto signUpDto) {
        endUserService.signUp(signUpDto);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Registered successfully!");
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<JWTToken> signIn(@Valid @RequestBody LoginDto loginDto) {
        String jwt = endUserService.getJwtToken(loginDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }
}
