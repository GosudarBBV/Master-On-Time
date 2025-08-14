package com.master.on.time.master.on.time.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO representing a specialist application")
public record SpecialistApplicationDto(

        @NotNull(message = "Specialist ID is required")
        @Schema(description = "ID of the specialist", example = "123")
        Long specialistId,

        @NotBlank(message = "Full name is required")
        @Schema(description = "Full name of the specialist", example = "John Smith")
        String fullName,

        @Email(message = "Contact email must be valid")
        @NotBlank(message = "Contact email is required")
        @Schema(description = "Contact email address", example = "john.smith@example.com")
        String contactEmail,

        @NotBlank(message = "Phone number is required")
        @Schema(description = "Phone number", example = "+1234567890")
        String phoneNumber,

        @Schema(description = "Credentials like diplomas or certificates",
                example = "Certified Hairdresser Level 3")
        String credentials,

        @NotBlank(message = "Status is required")
        @Schema(description = "Application status (PENDING, ACTIVE, REJECTED)", example = "PENDING")
        String status

) {
}
