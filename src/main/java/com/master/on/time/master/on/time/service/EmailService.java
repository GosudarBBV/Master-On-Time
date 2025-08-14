package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.BookingResponseDto;
import java.time.LocalDate;
import java.util.List;

public interface EmailService {
    void sendBookingConfirmationEmail(String to, BookingResponseDto bookingDto);

    void sendBookingCancellationEmail(String to, BookingResponseDto bookingDto);

    void sendRescheduleProposalEmail(String to, BookingResponseDto bookingDto);

    void sendRescheduleAcceptedEmail(String to, BookingResponseDto bookingDto);

    void sendRescheduleConfirmedEmail(String to, BookingResponseDto bookingDto);

    void sendRescheduleDeclinedEmail(String to, BookingResponseDto bookingDto);

    void sendReminderEmail(String to, BookingResponseDto bookingDto,
                           String reminderTime);

    void sendDailyBookingSummaryEmail(String to,
                                      List<BookingResponseDto> bookings, LocalDate date);

    void sendFollowUpEmail(String to, BookingResponseDto bookingDto);

    void sendPasswordResetLink(Long userId, String resetLink);

    void sendPasswordUpdateNotification(Long userId);
}
