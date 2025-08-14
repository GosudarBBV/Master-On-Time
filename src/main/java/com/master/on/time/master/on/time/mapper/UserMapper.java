package com.master.on.time.master.on.time.mapper;

import com.master.on.time.master.on.time.config.MapperConfig;
import com.master.on.time.master.on.time.dto.UserDetailsDto;
import com.master.on.time.master.on.time.dto.UserDto;
import com.master.on.time.master.on.time.dto.UserRegistrationRequestDto;
import com.master.on.time.master.on.time.dto.UserResponseDto;
import com.master.on.time.master.on.time.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toModel(UserRegistrationRequestDto user);

    UserResponseDto toResponseDto(User user);

    UserDto toUserDto(User user);

    UserDetailsDto toUserDetailsDto(User user);
}
