package com.example.demo.service.mapper;

import com.example.demo.model.EndUser;
import com.example.demo.service.dto.EndUserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EndUserMapper extends EntityMapper<EndUserDto, EndUser> {
}
