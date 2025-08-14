package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.BookingRequestDto;
import com.master.on.time.master.on.time.dto.BookingRescheduleRequestDto;
import com.master.on.time.master.on.time.dto.BookingResponseDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    List<LocalDateTime> getAvailableTimeSlots(Long specialistId,
                                              Long serviceItemId,
                                              LocalDate date);

    BookingResponseDto bookTimeSlot(Long clientId,
                                    BookingRequestDto requestDto);

    void cancelBooking(Long clientId,
                       Long bookingId);

    void proposeReschedule(Long specialistId,
                           BookingRescheduleRequestDto dto);

    void respondToReschedule(Long clientId,
                             Long bookingId,
                             boolean accept);

    BookingResponseDto blockTimeSlot(Long specialistId,
                                     LocalDateTime startTime,
                                     LocalDateTime endTime,
                                     String reason);

    void unblockTimeSlot(Long bookingId,
                         Long specialistId);

    List<BookingResponseDto> getConfirmedBookingsForUser(Long userId);

    void syncUserBookingsWithGoogleCalendar(Long userId);

    List<BookingResponseDto> getUpcomingAppointments(Long userId);

    List<BookingResponseDto> getUpcomingAppointmentsDto(Long userId);

    List<BookingResponseDto> getBookingsForSpecialistIdOnDate(Long specialistId,
                                                              LocalDate date);

    BookingResponseDto getBookingById(Long bookingId, Long userId);
}
