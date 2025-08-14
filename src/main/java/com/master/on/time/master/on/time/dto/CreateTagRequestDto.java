package com.master.on.time.master.on.time.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO for creating a new tag")
public record CreateTagRequestDto(

        @NotBlank(message = "Tag name is required")
        @Schema(description = "Name of the tag", example = "Urgent")
        String name

) {
}
