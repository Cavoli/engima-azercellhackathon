package com.example.demo.service.mapper;

import com.example.demo.model.EndUser;
import com.example.demo.service.dto.SignUpDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SignUpMapper extends EntityMapper<SignUpDto, EndUser>{
}
