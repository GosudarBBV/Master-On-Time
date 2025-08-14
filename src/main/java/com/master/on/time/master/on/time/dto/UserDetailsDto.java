package com.master.on.time.master.on.time.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Detailed information about a user")
public record UserDetailsDto(

        @NotNull
        @Schema(description = "User ID", example = "123")
        Long id,

        @NotBlank
        @Schema(description = "Username", example = "john_doe")
        String username,

        @Email
        @NotBlank
        @Schema(description = "User email", example = "john.doe@example.com")
        String email,

        @NotBlank
        @Schema(description = "Type of the user", example = "CLIENT")
        String userType,

        @NotBlank
        @Schema(description = "User status", example = "ACTIVE")
        String status,

        @NotNull
        @Schema(description = "Registration date", example = "2025-08-10T10:00:00")
        LocalDateTime registrationDate,

        @Schema(description = "List of user bookings (simplified as strings)",
                example = "[\"Booking1\", \"Booking2\"]")
        List<String> bookings,

        @Schema(description = "List of user reviews (simplified as strings)",
                example = "[\"Review1\", \"Review2\"]")
        List<String> reviews

) {
}
