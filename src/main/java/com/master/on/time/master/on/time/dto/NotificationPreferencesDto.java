package com.master.on.time.master.on.time.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User notification preferences")
public record NotificationPreferencesDto(

        @Schema(description = "Is email notification enabled", example = "true")
        boolean emailEnabled,

        @Schema(description = "Is SMS notification enabled", example = "false")
        boolean smsEnabled,

        @Schema(description = "Is in-app notification enabled", example = "true")
        boolean inAppEnabled

) {
}
