package com.master.on.time.master.on.time.dto;

import com.master.on.time.master.on.time.model.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;

@Schema(description = "DTO for updating user profile")
public record UserProfileUpdateRequestDto(

        @Schema(description = "User's first name", example = "John")
        String firstName,

        @Schema(description = "User's last name", example = "Doe")
        String lastName,

        @Email
        @Schema(description = "User's email", example = "john.doe@example.com")
        String email,

        @Schema(description = "User's phone number", example = "+1234567890")
        String phoneNumber,

        @Schema(description = "User address")
        AddressDto address,

        @Schema(description = "User's profile image in Base64 format")
        String profileImageBase64,

        @Schema(description = "User's date of birth", example = "1990-01-01")
        LocalDate dateOfBirth,

        @Schema(description = "User's gender", example = "MALE")
        Gender gender
) {
}
