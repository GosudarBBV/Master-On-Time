package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.PaymentCardRequestDto;
import com.master.on.time.master.on.time.dto.PaymentCardResponseDto;
import com.master.on.time.master.on.time.mapper.PaymentCardMapper;
import com.master.on.time.master.on.time.model.PaymentCard;
import com.master.on.time.master.on.time.model.User;
import com.master.on.time.master.on.time.repository.PaymentCardRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentCardServiceImpl implements PaymentCardService {
    private final PaymentCardRepository paymentCardRepository;
    private final PaymentCardMapper paymentCardMapper;
    private final UserService userService;

    @Override
    public PaymentCardResponseDto addCard(PaymentCardRequestDto requestDto) {
        Long userId = userService.getAuthenticatedUserId();
        User user = new User();
        user.setId(userId);

        PaymentCard card = new PaymentCard();
        card.setUser(user);
        card.setLastFourDigits(requestDto.cardNumber().substring(12));
        card.setCardType(detectCardType(requestDto.cardNumber()));
        card.setExpiryDate(requestDto.expiryDate());
        card.setPaymentToken("FAKE-TOKEN-" + System.currentTimeMillis());

        PaymentCard savedCard = paymentCardRepository.save(card);
        return paymentCardMapper.toDto(savedCard);
    }

    @Override
    public PaymentCardResponseDto updateCard(Long cardId, PaymentCardRequestDto requestDto) {
        Long userId = userService.getAuthenticatedUserId();
        PaymentCard card = paymentCardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        if (!card.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }

        card.setLastFourDigits(requestDto.cardNumber().substring(12));
        card.setCardType(detectCardType(requestDto.cardNumber()));
        card.setExpiryDate(requestDto.expiryDate());

        PaymentCard updatedCard = paymentCardRepository.save(card);
        return paymentCardMapper.toDto(updatedCard);
    }

    @Override
    public List<PaymentCardResponseDto> getUserCards() {
        Long userId = userService.getAuthenticatedUserId();
        return paymentCardRepository.findAllByUserId(userId)
                .stream()
                .map(paymentCardMapper::toDto)
                .toList();
    }

    @Override
    public void deleteCard(Long cardId) {
        Long userId = userService.getAuthenticatedUserId();
        PaymentCard card = paymentCardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        if (!card.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }
        paymentCardRepository.delete(card);
    }

    private String detectCardType(String cardNumber) {
        if (cardNumber.startsWith("4")) {
            return "VISA";
        }
        if (cardNumber.startsWith("5")) {
            return "MASTERCARD";
        }
        return "UNKNOWN";
    }
}
