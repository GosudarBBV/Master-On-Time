package com.master.on.time.master.on.time.security;

import com.master.on.time.master.on.time.dto.UserLoginRequestDto;
import com.master.on.time.master.on.time.dto.UserLoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserLoginResponseDto authenticate(UserLoginRequestDto requestDto) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.email(),requestDto.password())
        );

        String token = jwtUtil.generatedToken(authentication.getName());
        return new UserLoginResponseDto(token);
    }
}
