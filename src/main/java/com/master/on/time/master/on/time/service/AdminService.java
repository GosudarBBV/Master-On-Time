package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.UserDetailsDto;
import com.master.on.time.master.on.time.dto.UserDto;
import java.util.List;

public interface AdminService {
    List<UserDto> searchUsers(String username, String email, String userType, String status);

    UserDetailsDto getUserDetails(Long userId);

    void deactivateUser(Long userId);

    void reactivateUser(Long userId);

    void flagUser(Long userId);
}
