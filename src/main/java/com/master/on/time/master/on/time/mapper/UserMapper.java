package com.master.on.time.master.on.time.mapper;

import com.master.on.time.master.on.time.config.MapperConfig;
import com.master.on.time.master.on.time.dto.AddressDto;
import com.master.on.time.master.on.time.dto.UserDetailsDto;
import com.master.on.time.master.on.time.dto.UserDto;
import com.master.on.time.master.on.time.dto.UserRegistrationRequestDto;
import com.master.on.time.master.on.time.dto.UserResponseDto;
import com.master.on.time.master.on.time.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toModel(UserRegistrationRequestDto user);

    default UserResponseDto toResponseDto(User user) {
        AddressDto addressDto = null;
        if (user.getAddress() != null) {
            addressDto = new AddressDto(
                    user.getAddress().getCountry(),
                    user.getAddress().getStreet(),
                    user.getAddress().getCity(),
                    user.getAddress().getZip()
            );
        }

        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                addressDto,
                user.getPhoneNumber(),
                user.getProfileImageUrl()
        );
    }

    UserDto toUserDto(User user);

    UserDetailsDto toUserDetailsDto(User user);
}
