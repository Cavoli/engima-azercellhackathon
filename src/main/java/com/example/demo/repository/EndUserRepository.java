package com.example.demo.repository;

import com.example.demo.model.EndUser;

import java.util.Optional;

public interface EndUserRepository extends GenericRepository<EndUser>{
    Optional<EndUser> getEndUserByUsername(String username);
}
