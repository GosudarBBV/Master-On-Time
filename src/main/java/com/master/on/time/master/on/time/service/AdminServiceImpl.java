package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.UserDetailsDto;
import com.master.on.time.master.on.time.dto.UserDto;
import com.master.on.time.master.on.time.exception.EntityNotFoundException;
import com.master.on.time.master.on.time.mapper.UserMapper;
import com.master.on.time.master.on.time.model.User;
import com.master.on.time.master.on.time.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> searchUsers(String username, String email,
                                     String userType, String status) {
        List<User> users = userRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (username != null && !username.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("username")),
                        "%" + username.toLowerCase() + "%"));
            }
            if (email != null && !email.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("email")),
                        "%" + email.toLowerCase() + "%"));
            }
            if (userType != null && !userType.isBlank()) {
                predicates.add(cb.equal(cb.upper(root.get("role").get("name")),
                        userType.toUpperCase()));
            }
            if (status != null && !status.isBlank()) {
                if ("active".equalsIgnoreCase(status)) {
                    predicates.add(cb.isTrue(root.get("active")));
                } else if ("inactive".equalsIgnoreCase(status)) {
                    predicates.add(cb.isFalse(root.get("active")));
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        });

        return users.stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetailsDto getUserDetails(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()
                        -> new EntityNotFoundException("User not found with id: "
                        + userId));
        return userMapper.toUserDetailsDto(user);
    }

    @Override
    @Transactional
    public void deactivateUser(Long userId) {
        User user = getUserOrThrow(userId);
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void reactivateUser(Long userId) {
        User user = getUserOrThrow(userId);
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void flagUser(Long userId) {
        User user = getUserOrThrow(userId);
        user.setFlagged(true);
        userRepository.save(user);
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()
                        -> new EntityNotFoundException("User not found with id: "
                        + userId));
    }
}
