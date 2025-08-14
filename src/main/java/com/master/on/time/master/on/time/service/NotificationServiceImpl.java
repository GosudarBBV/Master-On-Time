package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.BookingResponseDto;
import com.master.on.time.master.on.time.dto.NotificationDto;
import com.master.on.time.master.on.time.dto.NotificationPreferencesDto;
import com.master.on.time.master.on.time.exception.EntityNotFoundException;
import com.master.on.time.master.on.time.mapper.NotificationMapper;
import com.master.on.time.master.on.time.model.Notification;
import com.master.on.time.master.on.time.model.User;
import com.master.on.time.master.on.time.repository.NotificationRepository;
import com.master.on.time.master.on.time.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepository;
    private final UserService userService;
    private final EmailService emailService;
    private final NotificationPreferencesService notificationPreferencesService;

    @Override
    public void createBookingNotification(Long specialistId,
                                          BookingResponseDto bookingDto) {
        saveNotification(specialistId,
                String.format("New booking confirmed: Service ID %d, Date: %s - %s, Client ID: %d",
                        bookingDto.serviceItemId(), bookingDto.startTime(),
                        bookingDto.endTime(), bookingDto.clientId()));
    }

    @Override
    public void createCancellationNotification(Long specialistId,
                                               BookingResponseDto bookingDto) {
        saveNotification(specialistId,
                String.format("Booking cancelled: Service '%s', Date: %s - %s, Client ID: %d",
                        bookingDto.serviceName(), bookingDto.startTime(),
                        bookingDto.endTime(), bookingDto.clientId()));
    }

    @Override
    public void createRescheduleProposalNotification(Long clientId,
                                                     BookingResponseDto bookingDto) {
        String reason = bookingDto.rescheduleMessage()
                != null ? bookingDto.rescheduleMessage() : "No reason provided";
        saveNotification(clientId,
                String.format("Your booking ID %d has a"
                                + " reschedule proposal for %s - %s. Reason: %s",
                        bookingDto.id(), bookingDto.startTime(),
                        bookingDto.endTime(), reason));
    }

    @Override
    public void createRescheduleAcceptedNotification(Long specialistId,
                                                     BookingResponseDto bookingDto) {
        saveNotification(specialistId,
                String.format("Client accepted the reschedule "
                                + "for booking ID %d. New time: %s - %s.",
                        bookingDto.id(), bookingDto.startTime(), bookingDto.endTime()));
    }

    @Override
    public void createRescheduleConfirmedNotification(Long clientId,
                                                      BookingResponseDto bookingDto) {
        saveNotification(clientId,
                String.format("Your booking ID %d has been rescheduled to %s - %s.",
                        bookingDto.id(), bookingDto.startTime(), bookingDto.endTime()));
    }

    @Override
    public void createRescheduleDeclinedNotification(Long specialistId,
                                                     BookingResponseDto bookingDto) {
        saveNotification(specialistId,
                String.format("Client declined the reschedule for booking ID %d.",
                        bookingDto.id()));
    }

    @Override
    public void createRescheduleDeclinedClientNotification(Long clientId,
                                                           BookingResponseDto bookingDto) {
        saveNotification(clientId,
                String.format("You declined the reschedule proposal for booking ID %d.",
                        bookingDto.id()));
    }

    @Override
    public List<NotificationDto> getUserNotifications(Long userId) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(findUserById(userId))
                .stream().map(notificationMapper::toDto).toList();
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public void deleteNotification(Long notificationId) {
        Long userId = userService.getAuthenticatedUserId();
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));
        if (!notification.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You don't have permission"
                    + " to delete this notification");
        }
        notificationRepository.delete(notification);
    }

    @Override
    public void createReminderNotification(Long userId, BookingResponseDto bookingDto,
                                           String reminderTime) {
        saveNotification(userId,
                String.format("Reminder: Your appointment for service '%s' "
                                + "with %s is scheduled in %s, on %s - %s.",
                        bookingDto.serviceName(), bookingDto.specialistName(),
                        reminderTime,
                        bookingDto.startTime(), bookingDto.endTime()));
    }

    @Override
    public void createDailyBookingSummaryNotification(Long specialistId,
                                                      List<BookingResponseDto> bookings,
                                                      LocalDate date) {
        if (bookings.isEmpty()) {
            return;
        }
        StringBuilder msg = new StringBuilder("Daily Booking Summary for ")
                .append(date)
                .append(":\n\n");
        bookings.forEach(b -> msg.append("Time: ").append(b.startTime().toLocalTime())
                .append(" - ").append(b.endTime().toLocalTime())
                .append("\nClient: ").append(b.clientId())
                .append("\nService: ").append(b.serviceName())
                .append("\n\n"));
        saveNotification(specialistId, msg.toString());
    }

    @Override
    public void createFollowUpNotification(Long userId, BookingResponseDto bookingDto) {
        saveNotification(userId,
                String.format("Thank you for your appointment on %s with %s. "
                                + "Please provide feedback or book your next service.",
                        bookingDto.startTime(), bookingDto.specialistName()));
    }

    @Override
    public void sendFollowUpNotification(BookingResponseDto bookingDto) {
        sendWithPreferences(bookingDto.clientId(),
                prefs -> {
                    if (prefs.emailEnabled()) {
                        emailService.sendFollowUpEmail(findUserById(bookingDto
                                        .clientId()).getEmail(), bookingDto);
                    }
                    if (prefs.inAppEnabled()) {
                        createFollowUpNotification(bookingDto.clientId(), bookingDto);
                    }
                });
    }

    @Override
    public void handleRescheduleConfirmed(Long clientId, BookingResponseDto bookingDto) {
        createRescheduleConfirmedNotification(clientId, bookingDto);
        emailService.sendRescheduleConfirmedEmail(findUserById(clientId).getEmail(),
                bookingDto);
    }

    @Override
    public void handleRescheduleDeclined(Long specialistId, BookingResponseDto bookingDto) {
        createRescheduleDeclinedNotification(specialistId, bookingDto);
        emailService.sendRescheduleDeclinedEmail(findUserById(specialistId).getEmail(),
                bookingDto);
    }

    private void saveNotification(Long userId, String message) {
        notificationRepository.save(Notification.builder()
                .user(findUserById(userId))
                .message(message)
                .createdAt(LocalDateTime.now())
                .read(false)
                .build());
    }

    private void sendWithPreferences(Long userId,
                                     Consumer<NotificationPreferencesDto> action) {
        action.accept(notificationPreferencesService.getPreferences(userId));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()
                        -> new EntityNotFoundException("User not found with id: "
                        + userId));
    }
}
