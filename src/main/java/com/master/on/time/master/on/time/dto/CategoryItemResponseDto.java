package com.master.on.time.master.on.time.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Service item within a category")
public record CategoryItemResponseDto(

        @NotNull
        @Schema(description = "Service item identifier", example = "10")
        Long id,

        @NotBlank
        @Schema(description = "Service item name", example = "Men's Haircut")
        String name,

        @NotNull
        @Positive(message = "Duration must be positive")
        @Schema(description = "Duration of the service in minutes", example = "30")
        Integer durationMinutes,

        @NotNull
        @Positive(message = "Price must be positive")
        @Schema(description = "Price of the service", example = "50.0")
        Double price

) {
}
