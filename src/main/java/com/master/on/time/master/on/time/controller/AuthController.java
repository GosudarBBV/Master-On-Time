package com.master.on.time.master.on.time.controller;

import com.master.on.time.master.on.time.dto.UserLoginRequestDto;
import com.master.on.time.master.on.time.dto.UserLoginResponseDto;
import com.master.on.time.master.on.time.dto.UserRegistrationRequestDto;
import com.master.on.time.master.on.time.dto.UserResponseDto;
import com.master.on.time.master.on.time.exception.RegistrationException;
import com.master.on.time.master.on.time.security.AuthenticationService;
import com.master.on.time.master.on.time.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for user registration and login")
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authService;

    @PostMapping("/registration")
    @Operation(summary = "Register a new user",
            description = "Creates a new user in the system")
    public UserResponseDto registerUser(@RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return userService.register(requestDto);
    }

    @Operation(
            summary = "Login user",
            description = "Authenticate user and return JWT token"
    )
    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto requestDto) {
        return authService.authenticate(requestDto);
    }
}
