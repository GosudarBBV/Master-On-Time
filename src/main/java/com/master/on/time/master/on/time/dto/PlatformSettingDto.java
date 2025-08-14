package com.master.on.time.master.on.time.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Schema(description = "Platform setting data transfer object")
@Builder
public record PlatformSettingDto(
        @Schema(description = "Unique identifier of the setting", example = "1")
        Long id,

        @NotBlank(message = "Key name is required")
        @Schema(description = "Key name of the setting",
                example = "max_login_attempts")
        String keyName,

        @NotBlank(message = "Value is required")
        @Schema(description = "Value of the setting", example = "5")
        String value,

        @Schema(description = "Description of the setting",
                example = "Maximum number of login attempts before lockout")
        String description,

        @NotBlank(message = "Value type is required")
        @Schema(description = "Type of the value", example = "Integer")
        String valueType
) {
}
