package com.master.on.time.master.on.time.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "User address data")
public record AddressDto(
        @NotBlank
        @Schema(description = "Country", example = "Ukraine")
        String country,

        @NotBlank
        @Schema(description = "City", example = "Kyiv")
        String city,

        @NotBlank
        @Schema(description = "Street", example = "Khreshchatyk 1")
        String street,

        @NotBlank
        @Schema(description = "ZIP code", example = "01001")
        String zip
) {
}
