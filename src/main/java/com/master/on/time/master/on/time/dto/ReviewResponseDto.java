package com.master.on.time.master.on.time.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "Response DTO for a review")
public record ReviewResponseDto(
        @NotNull
        @Schema(description = "ID of the review", example = "101")
        Long id,

        @NotNull
        @Schema(description = "ID of the client who wrote the review", example = "12")
        Long clientId,

        @NotNull
        @Schema(description = "ID of the specialist who received the review", example = "45")
        Long specialistId,

        @Min(value = 1, message = "Rating must be at least 1")
        @Max(value = 5, message = "Rating must be at most 5")
        @Schema(description = "Rating score from 1 to 5", example = "4")
        int rating,

        @Schema(description = "Review comment", example = "Very professional service.")
        String comment,

        @NotNull
        @Schema(description = "Date and time when the review was created",
                example = "2025-08-12T14:30:00")
        LocalDateTime createdAt
) {
}
