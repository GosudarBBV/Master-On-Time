package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.UserResponseDto;
import com.master.on.time.master.on.time.mapper.UserMapper;
import com.master.on.time.master.on.time.model.RoleName;
import com.master.on.time.master.on.time.model.User;
import com.master.on.time.master.on.time.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserResponseDto> searchSpecialistsByServiceAndLocation(String serviceName,
                                                                       String location) {
        if ((serviceName == null || serviceName.isBlank())
                && (location == null || location.isBlank())) {
            throw new IllegalArgumentException("At least one "
                    + "search parameter must be provided");
        }

        List<User> providers = userRepository.findProvidersByServiceAndLocation(
                RoleName.SPECIALIST,
                serviceName != null ? serviceName.trim() : null,
                location != null ? location.trim() : null
        );

        return providers.stream()
                .map(userMapper::toResponseDto)
                .toList();
    }

    public List<UserResponseDto> searchSpecialists(
            String serviceName,
            String firstName,
            String city,
            List<String> categories,
            Integer minExperience,
            Double minRating
    ) {
        if ((serviceName == null || serviceName.isBlank())
                && (firstName == null || firstName.isBlank())
                && (city == null || city.isBlank())
                && (categories == null || categories.isEmpty())
                && minExperience == null
                && minRating == null) {
            throw new IllegalArgumentException("At least one search parameter must be provided");
        }

        List<User> providers = userRepository.searchSpecialists(
                RoleName.SPECIALIST,
                serviceName != null ? serviceName.trim() : null,
                firstName != null ? firstName.trim() : null,
                city != null ? city.trim() : null,
                categories != null && !categories.isEmpty() ? categories : null,
                minExperience,
                minRating
        );

        return providers.stream()
                .map(userMapper::toResponseDto)
                .toList();
    }
}
