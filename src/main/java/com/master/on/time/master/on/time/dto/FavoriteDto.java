package com.master.on.time.master.on.time.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO for saved favorite users")
public record FavoriteDto(

        @NotNull(message = "Target user ID is required")
        @Schema(description = "Target user ID", example = "42")
        Long targetId,

        @NotBlank(message = "Target full name is required")
        @Schema(description = "Target full name", example = "John Doe")
        String targetName

) {
}
