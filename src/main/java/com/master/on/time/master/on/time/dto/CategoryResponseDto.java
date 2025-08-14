package com.master.on.time.master.on.time.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;

@Schema(description = "Service category")
public record CategoryResponseDto(

        @NotNull
        @Schema(description = "Category identifier", example = "1")
        Long id,

        @NotBlank
        @Schema(description = "Category name", example = "Haircuts")
        String name,

        @NotNull
        @Size(min = 1, message = "Category must contain at least one service item")
        @Schema(description = "List of services in the category")
        Set<CategoryItemResponseDto> items

) {
}
