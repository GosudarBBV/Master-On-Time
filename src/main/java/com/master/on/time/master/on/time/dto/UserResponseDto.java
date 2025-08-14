package com.master.on.time.master.on.time.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

@Schema(description = "Data transfer object representing user details")
public record UserResponseDto(
        @Schema(description = "User's ID", example = "123")
        Long id,

        @Email
        @Schema(description = "User's email", example = "john.doe@example.com")
        String email,

        @Schema(description = "User's first name", example = "John")
        String firstName,

        @Schema(description = "User's last name", example = "Doe")
        String lastName,

        @Schema(description = "User's address", example = "123 Main St")
        String address,

        @Schema(description = "User's country", example = "USA")
        String country,

        @Schema(description = "User's city", example = "New York")
        String city,

        @Schema(description = "User's phone number", example = "+1234567890")
        String phoneNumber,

        @Schema(description = "URL to user's profile image", example = "http://example.com/image.jpg")
        String profileImageUrl
) {
}
