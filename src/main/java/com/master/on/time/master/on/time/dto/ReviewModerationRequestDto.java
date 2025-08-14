package com.master.on.time.master.on.time.dto;

import com.master.on.time.master.on.time.model.ReviewStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request DTO for moderating a review")
public record ReviewModerationRequestDto(

        @NotNull(message = "Review status is required")
        @Schema(description = "New status of the review", example = "APPROVED")
        ReviewStatus status

) {
}
