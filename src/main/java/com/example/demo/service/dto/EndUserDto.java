package com.example.demo.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class EndUserDto {

    private Long id;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String username;

    private String password;

}
