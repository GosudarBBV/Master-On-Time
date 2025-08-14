package com.master.on.time.master.on.time.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "Time interval when specialist is unavailable (breaks, holidays)")
public record UnavailabilityRequestDto(
        @NotNull(message = "Start datetime is required")
        @Schema(description = "Start datetime of unavailability",
                example = "2025-08-10T13:00:00")
        LocalDateTime start,

        @NotNull(message = "End datetime is required")
        @Schema(description = "End datetime of unavailability",
                example = "2025-08-10T14:00:00")
        LocalDateTime end
) {
}
