package com.master.on.time.master.on.time.mapper;

import com.master.on.time.master.on.time.config.MapperConfig;
import com.master.on.time.master.on.time.dto.PaymentCardResponseDto;
import com.master.on.time.master.on.time.model.PaymentCard;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface PaymentCardMapper {
    PaymentCardResponseDto toDto(PaymentCard paymentCard);
}
