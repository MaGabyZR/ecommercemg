package com.magabyzr.ecommercemg.mappers;

import com.magabyzr.ecommercemg.dtos.UserDto;
import com.magabyzr.ecommercemg.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
