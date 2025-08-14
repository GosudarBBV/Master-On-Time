package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.BookingResponseDto;
import com.master.on.time.master.on.time.dto.NotificationDto;
import java.time.LocalDate;
import java.util.List;

public interface NotificationService {
    void createBookingNotification(Long specialistId, BookingResponseDto bookingDto);

    void createCancellationNotification(Long specialistId, BookingResponseDto bookingDto);

    List<NotificationDto> getUserNotifications(Long userId);

    void markAsRead(Long notificationId);

    void deleteNotification(Long notificationId);

    void createRescheduleProposalNotification(Long clientId, BookingResponseDto bookingDto);

    void createRescheduleAcceptedNotification(Long specialistId,
                                              BookingResponseDto bookingDto);

    void createRescheduleConfirmedNotification(Long clientId,
                                               BookingResponseDto bookingDto);

    void createRescheduleDeclinedNotification(Long specialistId,
                                              BookingResponseDto bookingDto);

    void createRescheduleDeclinedClientNotification(Long clientId,
                                                    BookingResponseDto bookingDto);

    void createReminderNotification(Long userId, BookingResponseDto bookingDto,
                                    String reminderTime);

    void createDailyBookingSummaryNotification(Long providerId,
                                               List<BookingResponseDto> bookings,
                                               LocalDate date);

    void createFollowUpNotification(Long userId, BookingResponseDto bookingDto);

    void sendFollowUpNotification(BookingResponseDto bookingDto);

    void handleRescheduleConfirmed(Long clientId, BookingResponseDto bookingDto);

    void handleRescheduleDeclined(Long specialistId, BookingResponseDto bookingDto);
}
