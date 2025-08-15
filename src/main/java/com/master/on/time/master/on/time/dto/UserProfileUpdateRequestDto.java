package com.master.on.time.master.on.time.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO for updating user profile")
public record UserProfileUpdateRequestDto(

        @NotBlank(message = "First name is required")
        @Schema(description = "User's first name", example = "John")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Schema(description = "User's last name", example = "Doe")
        String lastName,

        @Email(message = "Email should be valid")
        @NotBlank(message = "Email is required")
        @Schema(description = "User's email address",
                example = "john.doe@example.com")
        String email,

        @Schema(description = "User profile description",
                example = "Experienced specialist in hair styling")
        String description,

        @NotNull
        @Schema(description = "User address")
        AddressDto address

) {
}
