package com.master.on.time.master.on.time.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Data transfer object for Tag")
public record TagDto(

        @NotNull
        @Schema(description = "ID of the tag", example = "1")
        Long id,

        @NotBlank(message = "Tag name is required")
        @Schema(description = "Name of the tag", example = "Urgent")
        String name

) {
}
