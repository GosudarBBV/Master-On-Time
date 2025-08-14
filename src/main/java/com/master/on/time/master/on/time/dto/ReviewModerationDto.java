package com.master.on.time.master.on.time.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO for moderating a review")
public record ReviewModerationDto(

        @NotNull(message = "Review ID is required")
        @Schema(description = "ID of the review", example = "101")
        Long id,

        @NotBlank(message = "Review text is required")
        @Schema(description = "Text content of the review", example = "Great service!")
        String text,

        @NotBlank(message = "Status is required")
        @Schema(description = "Moderation status", example = "APPROVED")
        String status,

        @NotNull(message = "User ID is required")
        @Schema(description = "ID of the user who wrote the review", example = "42")
        Long userId,

        @NotNull(message = "Service ID is required")
        @Schema(description = "ID of the service being reviewed", example = "5")
        Long serviceId

) {
}
