package com.master.on.time.master.on.time.repository;

import com.master.on.time.master.on.time.model.PaymentCard;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentCardRepository extends JpaRepository<PaymentCard, Long> {
    List<PaymentCard> findAllByUserId(Long userId);
}
