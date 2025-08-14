package com.master.on.time.master.on.time.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO for updating a category")
public record UpdateCategoryRequestDto(

        @NotBlank(message = "Category name is required")
        @Schema(description = "Name of the category", example = "Haircuts")
        String name

) {
}
