package com.master.on.time.master.on.time.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO for user login request")
public record UserLoginRequestDto(

        @Email
        @NotBlank
        @Schema(example = "john.doe@example.com", description = "User email")
        String email,

        @NotBlank
        @Schema(example = "securePassword123", description = "User password")
        String password

) {
}
