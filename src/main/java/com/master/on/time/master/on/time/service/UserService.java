package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.UserProfileUpdateRequestDto;
import com.master.on.time.master.on.time.dto.UserRegistrationRequestDto;
import com.master.on.time.master.on.time.dto.UserResponseDto;
import java.util.List;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto);

    void deleteById(Long id);

    Long getAuthenticatedUserId();

    UserResponseDto getUserById(Long id);

    List<UserResponseDto> getAllSpecialists();

    UserResponseDto updateUserProfile(Long userId, UserProfileUpdateRequestDto dto);

    UserResponseDto updateVisibilityStatus(Long userId, boolean visible);

    void deleteAccount(Long userId);
}
