package com.example.demo.service.impl;


import com.example.demo.exception.BadRequestAlertException;
import com.example.demo.model.EndUser;
import com.example.demo.repository.EndUserRepository;
import com.example.demo.security.jwt.JWTToken;
import com.example.demo.security.jwt.JwtTokenProvider;
import com.example.demo.service.dto.LoginDto;
import com.example.demo.service.dto.SignUpDto;
import com.example.demo.service.mapper.SignUpMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EndUserService {
    private final EndUserRepository repository;
    private final SignUpMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public EndUserService(EndUserRepository repository,
                          SignUpMapper mapper,
                          PasswordEncoder passwordEncoder,
                          JwtTokenProvider tokenProvider,
                          AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @Transactional
    public void signUp(SignUpDto signUpDto) {
        repository.getEndUserByUsername(signUpDto.getUsername()).ifPresent(
                endUser -> {
                    throw new BadRequestAlertException("This user exists", "userService", "same.user");
                });
        EndUser user = mapper.toEntity(signUpDto);
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        repository.save(user);
    }

    @Transactional(readOnly = true)
    public String getJwtToken(LoginDto loginDto) {
        EndUser user = repository.getEndUserByUsername(loginDto.getUsername())
                .orElseThrow(() ->new BadRequestAlertException("This user doesn't exist", "userService", "not.found"));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(),
                loginDto.getPassword()
        );
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.createToken(authentication, false, user.getId());
    }
}
