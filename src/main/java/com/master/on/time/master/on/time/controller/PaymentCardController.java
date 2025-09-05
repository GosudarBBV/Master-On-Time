package com.master.on.time.master.on.time.controller;

import com.master.on.time.master.on.time.dto.PaymentCardRequestDto;
import com.master.on.time.master.on.time.dto.PaymentCardResponseDto;
import com.master.on.time.master.on.time.service.PaymentCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
@Tag(name = "Payment Cards", description = "Endpoints for managing payment cards")
public class PaymentCardController {
    private final PaymentCardService paymentCardService;

    @Operation(summary = "Add a new payment card")
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public PaymentCardResponseDto addCard(
            @RequestBody
            @Valid
            PaymentCardRequestDto requestDto) {
        return paymentCardService.addCard(requestDto);
    }

    @Operation(summary = "Update a payment card of the authenticated user")
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{cardId}")
    public PaymentCardResponseDto updateCard(
            @PathVariable Long cardId,
            @RequestBody @Valid PaymentCardRequestDto requestDto) {

        return paymentCardService.updateCard(cardId, requestDto);
    }

    @Operation(summary = "Get all cards of the authenticated user")
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public List<PaymentCardResponseDto> getUserCards() {
        return paymentCardService.getUserCards();
    }

    @Operation(summary = "Delete a payment card")
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    public void deleteCard(@PathVariable Long id) {
        paymentCardService.deleteCard(id);
    }
}
