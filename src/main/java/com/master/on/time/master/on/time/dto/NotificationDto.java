package com.master.on.time.master.on.time.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "Notification data transfer object")
public record NotificationDto(
        @NotNull
        @Schema(description = "Notification id", example = "123")
        Long id,

        @NotBlank(message = "Notification message is required")
        @Schema(description = "Notification message",
                example = "Your appointment is confirmed.")
        String message,

        @NotNull
        @Schema(description = "Date and time when notification was created",
                example = "2025-08-12T20:45:30")
        LocalDateTime createdAt,

        @Schema(description = "Read status of notification", example = "false")
        boolean read
) {
}
