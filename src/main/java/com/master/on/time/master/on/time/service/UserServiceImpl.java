package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.UserProfileUpdateRequestDto;
import com.master.on.time.master.on.time.dto.UserRegistrationRequestDto;
import com.master.on.time.master.on.time.dto.UserResponseDto;
import com.master.on.time.master.on.time.exception.EntityNotFoundException;
import com.master.on.time.master.on.time.exception.RegistrationException;
import com.master.on.time.master.on.time.exception.UserRoleNotAllowedException;
import com.master.on.time.master.on.time.mapper.UserMapper;
import com.master.on.time.master.on.time.model.Role;
import com.master.on.time.master.on.time.model.RoleName;
import com.master.on.time.master.on.time.model.User;
import com.master.on.time.master.on.time.repository.RoleRepository;
import com.master.on.time.master.on.time.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.email())) {
            throw new RegistrationException("User already exists with email: "
                    + requestDto.email());
        }

        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new EntityNotFoundException("Role with name "
                        + RoleName.USER + " not found"));

        user.setRoles(Set.of(userRole));
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = findUserById(id);
        return userMapper.toResponseDto(user);
    }

    @Override
    public UserResponseDto updateUserProfile(Long userId,
                                             UserProfileUpdateRequestDto dto) {
        User user = findUserById(userId);

        if (!user.getEmail().equals(dto.email())
                && userRepository.existsByEmail(dto.email())) {
            throw new RegistrationException("Email already in use: "
                    + dto.email());
        }

        updateUserByRole(user, dto);

        User updated = userRepository.save(user);
        return userMapper.toResponseDto(updated);
    }

    private RoleName getPrimaryRole(User user) {
        return user.getRoles().stream()
                .map(Role::getName)
                .filter(r -> r == RoleName.SPECIALIST || r == RoleName.USER)
                .findFirst()
                .orElse(null);
    }

    private void updateUserByRole(User user, UserProfileUpdateRequestDto dto) {
        RoleName role = getPrimaryRole(user);

        if (role == RoleName.USER || role == RoleName.SPECIALIST) {
            user.setFirstName(dto.firstName());
            user.setLastName(dto.lastName());
            user.setEmail(dto.email());
        } else {
            throw new UserRoleNotAllowedException("User role not allowed");
        }
    }

    @Override
    public UserResponseDto updateVisibilityStatus(Long userId,
                                                  boolean visible) {
        User user = findUserById(userId);

        boolean isSpecialist = user.getRoles().stream()
                .anyMatch(r -> r.getName() == RoleName.SPECIALIST);

        if (!isSpecialist) {
            throw new UserRoleNotAllowedException("Only specialists"
                    + " can change visibility");
        }

        user.setVisible(visible);
        User updated = userRepository.save(user);
        return userMapper.toResponseDto(updated);
    }

    @Override
    public void deleteAccount(Long userId) {
        User user = findUserById(userId);
        user.setDeleted(true);
        userRepository.save(user);
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()
                        -> new EntityNotFoundException("User not found with id: "
                        + id));
    }
}
