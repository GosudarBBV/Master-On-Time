package com.master.on.time.master.on.time.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO for updating review text")
public record UpdateReviewRequestDto(

        @Size(max = 1000, message = "Text is too long")
        @Schema(description = "Updated review text", example = "Updated comment about the service")
        String text

) {
}
