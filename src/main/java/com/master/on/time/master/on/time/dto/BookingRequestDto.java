package com.master.on.time.master.on.time.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "Request to book a specific service at a given time")
public record BookingRequestDto(

        @NotNull(message = "Specialist ID is required")
        @Schema(description = "ID of the specialist to book with", example = "12")
        Long specialistId,

        @NotNull(message = "Service item ID is required")
        @Schema(description = "ID of the service item to book", example = "5")
        Long serviceItemId,

        @NotNull(message = "Start time is required")
        @Schema(description = "Start time of the booking", example = "2025-08-10T14:00:00")
        LocalDateTime startTime

) {
}
