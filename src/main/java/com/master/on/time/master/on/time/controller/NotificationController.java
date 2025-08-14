package com.master.on.time.master.on.time.controller;

import com.master.on.time.master.on.time.dto.BookingResponseDto;
import com.master.on.time.master.on.time.dto.NotificationDto;
import com.master.on.time.master.on.time.service.BookingService;
import com.master.on.time.master.on.time.service.NotificationService;
import com.master.on.time.master.on.time.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Endpoints for user notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;
    private final BookingService bookingService;

    @PreAuthorize("hasAnyRole('USER', 'SPECIALIST')")
    @Operation(summary = "Get all notifications for the authenticated user")
    @GetMapping
    public List<NotificationDto> getNotifications() {
        Long userId = userService.getAuthenticatedUserId();
        return notificationService.getUserNotifications(userId);
    }

    @PreAuthorize("hasAnyRole('USER', 'SPECIALIST')")
    @Operation(summary = "Mark a notification as read")
    @PostMapping("/{notificationId}/read")
    public void markNotificationAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
    }

    @PreAuthorize("hasAnyRole('USER', 'SPECIALIST')")
    @Operation(summary = "Delete a notification")
    @DeleteMapping("/{notificationId}")
    public void deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
    }

    @PreAuthorize("hasAnyRole('USER', 'SPECIALIST')")
    @Operation(summary = "Send follow-up notification after booking completion")
    @PostMapping("/{bookingId}/send-follow-up")
    public void sendFollowUpNotification(
            @Parameter(description = "ID of the booking", required = true)
            @PathVariable Long bookingId) {
        Long userId = userService.getAuthenticatedUserId();
        BookingResponseDto bookingDto = bookingService.getBookingById(bookingId, userId);
        notificationService.sendFollowUpNotification(bookingDto);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Confirm reschedule of a booking by client")
    @PostMapping("/{bookingId}/reschedule/confirm")
    public void confirmReschedule(
            @Parameter(description = "ID of the booking", required = true)
            @PathVariable Long bookingId) {
        Long userId = userService.getAuthenticatedUserId();
        BookingResponseDto bookingDto = bookingService.getBookingById(bookingId, userId);
        notificationService.handleRescheduleConfirmed(userId, bookingDto);
    }

    @PreAuthorize("hasRole('SPECIALIST')")
    @Operation(summary = "Decline reschedule of a booking by specialist")
    @PostMapping("/{bookingId}/reschedule/decline")
    public void declineReschedule(
            @Parameter(description = "ID of the booking", required = true)
            @PathVariable Long bookingId) {
        Long userId = userService.getAuthenticatedUserId();
        BookingResponseDto bookingDto = bookingService.getBookingById(bookingId, userId);
        notificationService.handleRescheduleDeclined(userId, bookingDto);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Create notification when client declines reschedule proposal")
    @PostMapping("/{bookingId}/reschedule/decline-client-notification")
    public void declineRescheduleClientNotification(
            @Parameter(description = "ID of the booking", required = true)
            @PathVariable Long bookingId) {
        Long userId = userService.getAuthenticatedUserId();
        BookingResponseDto bookingDto = bookingService.getBookingById(bookingId, userId);
        notificationService.createRescheduleDeclinedClientNotification(userId, bookingDto);
    }
}
