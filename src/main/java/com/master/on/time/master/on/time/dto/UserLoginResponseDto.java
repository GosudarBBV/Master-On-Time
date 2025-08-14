package com.master.on.time.master.on.time.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO for user login response containing JWT token")
public record UserLoginResponseDto(
        @Schema(description = "JWT authentication token",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token
) {
}
