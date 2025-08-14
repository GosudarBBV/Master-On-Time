package com.master.on.time.master.on.time.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO for updating a tag")
public record UpdateTagRequestDto(

        @NotBlank(message = "Tag name is required")
        @Schema(description = "Name of the tag", example = "Urgent")
        String name

) {
}
