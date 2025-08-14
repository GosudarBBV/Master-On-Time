package com.master.on.time.master.on.time.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Schema(description = "Request to block a time slot")
public record BlockTimeSlotRequestDto(

        @NotNull(message = "Start time is required")
        @Schema(description = "Start of the blocked time slot", example = "2025-08-12T09:00:00")
        LocalDateTime startTime,

        @NotNull(message = "End time is required")
        @Schema(description = "End of the blocked time slot", example = "2025-08-12T11:00:00")
        LocalDateTime endTime,

        @Size(max = 500, message = "Reason is too long")
        @Schema(description = "Reason for blocking the time slot", example = "Maintenance work")
        String reason

) {
}
