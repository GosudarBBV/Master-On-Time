package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.PaymentCardRequestDto;
import com.master.on.time.master.on.time.dto.PaymentCardResponseDto;
import java.util.List;

public interface PaymentCardService {
    PaymentCardResponseDto addCard(PaymentCardRequestDto requestDto);

    List<PaymentCardResponseDto> getUserCards();

    void deleteCard(Long cardId);
}
