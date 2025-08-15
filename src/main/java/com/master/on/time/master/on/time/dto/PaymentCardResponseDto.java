package com.master.on.time.master.on.time.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response DTO containing payment card information")
public record PaymentCardResponseDto(
        @Schema(description = "Unique card ID in the system", example = "101")
        Long id,

        @Schema(description = "Last 4 digits of the card", example = "1234")
        String lastFourDigits,

        @Schema(description = "Type of the card (VISA, MASTERCARD, UNKNOWN)",
                example = "VISA")
        String cardType,

        @Schema(description = "Card expiry date in MM/YY format",
                example = "12/26")
        String expiryDate
) {
}
