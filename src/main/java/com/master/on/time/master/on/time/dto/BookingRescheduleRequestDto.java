package com.master.on.time.master.on.time.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Schema(description = "Request to reschedule an existing booking")
public record BookingRescheduleRequestDto(

        @NotNull(message = "Booking ID is required")
        @Schema(description = "ID of the booking to reschedule", example = "123")
        Long bookingId,

        @NotNull(message = "Proposed start time is required")
        @Schema(description = "Proposed new start time", example = "2025-08-15T10:00:00")
        LocalDateTime proposedStartTime,

        @NotNull(message = "Proposed end time is required")
        @Schema(description = "Proposed new end time", example = "2025-08-15T11:00:00")
        LocalDateTime proposedEndTime,

        @Size(max = 500, message = "Message is too long")
        @Schema(description = "Optional message explaining the reschedule",
                example = "Need to move due to conflict")
        String message

) {
}
