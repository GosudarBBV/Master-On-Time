package com.master.on.time.master.on.time.dto;

import com.master.on.time.master.on.time.model.ReviewStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "Response DTO for review moderation details")
public record ReviewModerationResponseDto(

        @NotNull
        @Schema(description = "ID of the review", example = "101")
        Long id,

        @Min(value = 1, message = "Rating must be at least 1")
        @Max(value = 5, message = "Rating must be at most 5")
        @Schema(description = "Rating given in the review", example = "4")
        int rating,

        @NotBlank
        @Schema(description = "Comment text of the review",
                example = "Very professional service.")
        String comment,

        @NotNull
        @Schema(description = "Date and time when the review was created",
                example = "2025-08-12T14:30:00")
        LocalDateTime createdAt,

        @NotBlank
        @Schema(description = "Full name of the client who wrote the review",
                example = "John Doe")
        String clientName,

        @NotNull
        @Schema(description = "Current status of the review", example = "PENDING")
        ReviewStatus status

) {
}
