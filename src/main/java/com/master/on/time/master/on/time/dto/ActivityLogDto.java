package com.master.on.time.master.on.time.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Builder;

@Schema(description = "DTO for activity log entry")
@Builder
public record ActivityLogDto(
        @Schema(description = "Unique ID of the log entry", example = "123")
        Long id,

        @NotNull(message = "Timestamp is required")
        @Schema(description = "Timestamp of the action", example = "2025-08-12T20:45:30")
        LocalDateTime timestamp,

        @NotNull(message = "User ID is required")
        @Schema(description = "ID of the user who performed the action", example = "42")
        Long userId,

        @NotBlank(message = "Username is required")
        @Schema(description = "Username of the user", example = "johndoe")
        String username,

        @NotBlank(message = "Role is required")
        @Schema(description = "Role of the user", example = "ADMIN")
        String role,

        @NotBlank(message = "Action type is required")
        @Schema(description = "Type of the action performed", example = "CREATE")
        String actionType,

        @NotBlank(message = "Affected entity type is required")
        @Schema(description = "Type of the affected entity", example = "Booking")
        String affectedEntityType,

        @NotBlank(message = "Affected entity ID is required")
        @Schema(description = "ID of the affected entity", example = "987")
        String affectedEntityId,

        @Size(max = 1000, message = "Description is too long")
        @Schema(description = "Detailed description of the action",
                example = "Created new booking for client.")
        String description,

        @Pattern(regexp = "^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$",
                message = "IP address must be valid IPv4")
        @Schema(description = "IP address from where the action was performed",
                example = "192.168.0.1")
        String ipAddress
) {
}
