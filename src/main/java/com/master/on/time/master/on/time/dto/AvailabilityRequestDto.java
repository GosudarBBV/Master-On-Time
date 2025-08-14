package com.master.on.time.master.on.time.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Schema(description = "Working day and hours availability")
public record AvailabilityRequestDto(

        @NotNull(message = "Day of week is required")
        @Schema(description = "Day of the week", example = "MONDAY")
        DayOfWeek dayOfWeek,

        @NotNull(message = "Start time is required")
        @Schema(description = "Start time (e.g., 09:00)", example = "09:00")
        LocalTime startTime,

        @NotNull(message = "End time is required")
        @Schema(description = "End time (e.g., 17:00)", example = "17:00")
        LocalTime endTime

) {
}

