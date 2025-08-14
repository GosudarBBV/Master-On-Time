package com.master.on.time.master.on.time.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "Represents a confirmed booking between a client and a specialist")
public record BookingResponseDto(

        @NotNull
        @Schema(description = "ID of the booking", example = "101")
        Long id,

        @NotNull
        @Schema(description = "ID of the client who booked the service", example = "12")
        Long clientId,

        @NotNull
        @Schema(description = "ID of the service item", example = "3")
        Long serviceItemId,

        @NotNull
        @Schema(description = "ID of the specialist providing the service", example = "45")
        Long specialistId,

        @NotBlank
        @Schema(description = "Full name of the specialist", example = "John Smith")
        String specialistName,

        @NotBlank
        @Schema(description = "Name of the service booked", example = "Haircut")
        String serviceName,

        @NotBlank
        @Schema(description = "Price of the service", example = "25.00")
        String price,

        @NotNull
        @Schema(description = "Start time of the booking", example = "2025-08-10T14:00:00")
        LocalDateTime startTime,

        @NotNull
        @Schema(description = "End time of the booking", example = "2025-08-10T15:00:00")
        LocalDateTime endTime,

        @Schema(description = "Message regarding rescheduling", example = "Request to move booking")
        String rescheduleMessage,

        @NotBlank
        @Schema(description = "Booking status", example = "CONFIRMED")
        String status

) {
}
