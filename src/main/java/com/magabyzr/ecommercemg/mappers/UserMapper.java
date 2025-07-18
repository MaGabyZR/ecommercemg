package com.magabyzr.ecommercemg.mappers;

import com.magabyzr.ecommercemg.dtos.RegisterUserRequest;
import com.magabyzr.ecommercemg.dtos.UpdateUserRequest;
import com.magabyzr.ecommercemg.dtos.UserDto;
import com.magabyzr.ecommercemg.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    //@Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    UserDto toDto(User user);
    User toEntity(RegisterUserRequest request);
    void update(UpdateUserRequest request, @MappingTarget User user);
}
