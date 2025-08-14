package com.master.on.time.master.on.time.mapper;

import com.master.on.time.master.on.time.config.MapperConfig;
import com.master.on.time.master.on.time.dto.BookingResponseDto;
import com.master.on.time.master.on.time.model.Booking;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookingMapper {
    default BookingResponseDto toDto(Booking booking) {
        return new BookingResponseDto(
                booking.getId(),
                booking.getClient().getId(),
                booking.getServiceItem().getId(),
                booking.getSpecialist().getId(),
                booking.getSpecialist()
                        .getFirstName() + " " + booking
                        .getSpecialist().getLastName(),
                booking.getServiceItem().getName(),
                booking.getServiceItem().getPrice().toString(),
                booking.getStartTime(),
                booking.getEndTime(),
                null,
                booking.getStatus()
        );
    }

    default BookingResponseDto toBookingResponseDto(Booking booking, boolean isUser) {
        Long clientId = isUser ? booking.getSpecialist().getId() : booking.getClient().getId();
        String specialistName = isUser
                ? booking.getSpecialist().getFirstName() + " "
                + booking.getSpecialist().getLastName()
                : booking.getClient().getFirstName() + " "
                + booking.getClient().getLastName();

        return new BookingResponseDto(
                booking.getId(),
                clientId,
                booking.getServiceItem().getId(),
                booking.getSpecialist().getId(),
                specialistName,
                booking.getServiceItem().getName(),
                booking.getServiceItem().getPrice().toString(),
                booking.getStartTime(),
                booking.getEndTime(),
                null,
                booking.getStatus()
        );
    }
}
