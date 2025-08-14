package com.master.on.time.master.on.time.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Schema(description = "Request DTO for submitting a review")
public record ReviewRequestDto(

        @Min(value = 1, message = "Rating must be at least 1")
        @Max(value = 5, message = "Rating must be at most 5")
        @Schema(description = "Rating score from 1 to 5", example = "4")
        int rating,

        @Size(max = 1000, message = "Comment is too long")
        @Schema(description = "Review comment", example = "Great service!")
        String comment,

        @Schema(description = "Review submission date", example = "2025-08-12T14:30:00")
        LocalDateTime createdAt,

        @NotNull(message = "Booking ID is required")
        @Schema(description = "ID of the related booking", example = "1001")
        Long bookingId

) {
}
