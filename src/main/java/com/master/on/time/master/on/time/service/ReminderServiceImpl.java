package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.BookingResponseDto;
import com.master.on.time.master.on.time.mapper.BookingMapper;
import com.master.on.time.master.on.time.model.Booking;
import com.master.on.time.master.on.time.model.User;
import com.master.on.time.master.on.time.repository.BookingRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReminderServiceImpl implements ReminderService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final EmailService emailService;
    private final NotificationService notificationService;

    @Override
    public void sendReminders() {
        log.info("Starting reminder sending process...");
        LocalDateTime now = LocalDateTime.now();
        sendRemindersForTimeWindow(now.plusHours(24), "24 hours");
        sendRemindersForTimeWindow(now.plusHours(1), "1 hour");
        log.info("Reminder sending process completed.");
    }

    private void sendRemindersForTimeWindow(LocalDateTime targetTime,
                                            String reminderLabel) {
        LocalDateTime windowStart = targetTime.minusMinutes(5);
        LocalDateTime windowEnd = targetTime.plusMinutes(5);

        List<Booking> bookings = bookingRepository
                .findConfirmedBookingsBetween(windowStart, windowEnd);
        if (bookings.isEmpty()) {
            log.debug("No confirmed bookings found for {} reminder window.", reminderLabel);
            return;
        }

        bookings.forEach(booking -> {
            BookingResponseDto dto = bookingMapper.toDto(booking);
            String clientEmail = booking.getClient().getEmail();

            emailService.sendReminderEmail(clientEmail, dto, reminderLabel);
            notificationService.createReminderNotification(booking.getClient().getId(),
                    dto, reminderLabel);
        });

        log.info("Sent {} reminders for {} window.", bookings.size(), reminderLabel);
    }

    @Override
    public void sendDailySummaries() {
        log.info("Starting daily summary sending process...");
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        List<Booking> bookings = bookingRepository
                .findConfirmedBookingsBetween(startOfDay, endOfDay);
        if (bookings.isEmpty()) {
            log.debug("No confirmed bookings found for daily summaries.");
            return;
        }

        Map<User, List<BookingResponseDto>> bookingsBySpecialist = bookings.stream()
                .collect(Collectors.groupingBy(
                        Booking::getSpecialist,
                        Collectors.mapping(bookingMapper::toDto, Collectors.toList())
                ));

        bookingsBySpecialist.forEach((specialist, bookingDtos) -> {
            emailService.sendDailyBookingSummaryEmail(specialist.getEmail(), bookingDtos, today);
            notificationService.createDailyBookingSummaryNotification(specialist.getId(),
                    bookingDtos, today);
        });

        log.info("Daily summaries sent to {} specialists.", bookingsBySpecialist.size());
    }

    @Override
    public void sendFollowUpMessages() {
        log.info("Starting follow-up message sending process...");
        int count = sendFollowUpMessagesInternal();
        log.info("Follow-up messages sent: {}", count);
    }

    @Override
    public int sendFollowUpMessagesAndReturnCount() {
        return sendFollowUpMessagesInternal();
    }

    private int sendFollowUpMessagesInternal() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from = now.minusHours(24);
        LocalDateTime to = now;

        List<Booking> bookingsToNotify = bookingRepository
                .findCompletedBookingsWithoutFollowUp(from, to);
        if (bookingsToNotify.isEmpty()) {
            log.debug("No completed bookings found for follow-up.");
            return 0;
        }

        bookingsToNotify.forEach(booking -> {
            BookingResponseDto dto = bookingMapper.toDto(booking);
            emailService.sendFollowUpEmail(booking.getClient().getEmail(),
                    dto);
            notificationService.createFollowUpNotification(booking.getClient().getId(),
                    dto);
            booking.setFollowUpSent(true);
            bookingRepository.save(booking);
        });
        return bookingsToNotify.size();
    }
}
