package com.master.on.time.master.on.time.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Request DTO for adding a new payment card")
public record PaymentCardRequestDto(
        @Schema(description = "Full 16-digit card number",
                example = "4111111111111111")
        @NotBlank(message = "Card number is required")
        @Pattern(regexp = "\\d{16}", message = "Card number must be 16 digits")
        String cardNumber,

        @Schema(description = "Card expiry date in MM/YY format", example = "12/26")
        @NotBlank(message = "Expiry date is required")
        @Pattern(regexp = "(0[1-9]|1[0-2])/[0-9]{2}",
                message = "Expiry date must be in format MM/YY")
        String expiryDate,

        @Schema(description = "Card holder's full name", example = "John Doe")
        @NotBlank(message = "Card holder name is required")
        @Size(min = 2, max = 50,
                message = "Card holder name must be between 2 and 50 characters")
        String cardHolderName,

        @Schema(description = "3-digit security code on the back of the card",
                example = "123")
        @NotBlank(message = "Security code is required")
        @Pattern(regexp = "\\d{3}", message = "Security code must be 3 digits")
        String securityCode
) {
}
