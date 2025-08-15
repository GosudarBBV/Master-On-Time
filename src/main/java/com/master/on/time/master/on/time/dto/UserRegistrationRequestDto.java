package com.master.on.time.master.on.time.dto;

import com.master.on.time.master.on.time.validation.FieldMatch;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@FieldMatch(first = "password", second = "repeatPassword",
        message = "Passwords do not match")
@Schema(description = "DTO for user registration request")
public record UserRegistrationRequestDto(
        @NotBlank
        @Email
        @Schema(description = "User email address", example = "john.doe@example.com")
        String email,

        @NotBlank
        @Schema(description = "User password", example = "securePassword123")
        String password,

        @NotBlank
        @Schema(description = "Repeat password for confirmation", example = "securePassword123")
        String repeatPassword,

        @NotBlank
        @Schema(description = "User first name", example = "John")
        String firstName,

        @NotBlank
        @Schema(description = "User last name", example = "Doe")
        String lastName,

        @NotNull
        @Schema(description = "User address")
        AddressDto address,

        @Schema(description = "User phone number", example = "+1234567890")
        String phoneNumber,

        @Schema(description = "URL of the user's profile image",
                example = "http://example.com/image.jpg")
        String profileImageUrl

) {
}
