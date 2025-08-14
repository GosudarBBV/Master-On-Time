package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.BookingResponseDto;
import com.master.on.time.master.on.time.model.User;
import com.master.on.time.master.on.time.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

    @Override
    public void sendBookingConfirmationEmail(String to,
                                             BookingResponseDto bookingDto) {
        String subject = "Booking Confirmation";
        String body = String.format(
                "Dear Client,%nYour booking has been confirmed.%n"
                        + "Service: %s%nSpecialist: %s%nDate: "
                        + "%s - %s%nPrice: %s%nBooking ID: %d%n",
                bookingDto.serviceName(),
                bookingDto.specialistName(),
                bookingDto.startTime(),
                bookingDto.endTime(),
                bookingDto.price(),
                bookingDto.id()
        );
        sendEmail(to, subject, body);
    }

    @Override
    public void sendBookingCancellationEmail(String to,
                                             BookingResponseDto bookingDto) {
        String subject = "Booking Cancellation";
        String body = String.format(
                "Dear Client,%nYour booking has been cancelled.%n"
                        + "Service: %s%nSpecialist: %s%nDate: "
                        + "%s - %s%nBooking ID: %d%n",
                bookingDto.serviceName(),
                bookingDto.specialistName(),
                bookingDto.startTime(),
                bookingDto.endTime(),
                bookingDto.id()
        );
        sendEmail(to, subject, body);
    }

    @Override
    public void sendRescheduleProposalEmail(String to, BookingResponseDto bookingDto) {
        String subject = "Booking Reschedule Proposal";
        String body = String.format(
                "Dear Client,%nYou have a reschedule proposal for your booking ID %d.%n"
                        + "New proposed time: %s - %s%nPlease "
                        + "respond in your dashboard.%nReason: %s",
                bookingDto.id(),
                bookingDto.startTime(),
                bookingDto.endTime(),
                bookingDto.rescheduleMessage() != null
                        ? bookingDto.rescheduleMessage() : "No reason provided"
        );
        sendEmail(to, subject, body);
    }

    @Override
    public void sendRescheduleAcceptedEmail(String to,
                                            BookingResponseDto bookingDto) {
        String subject = "Booking Reschedule Accepted";
        String body = String.format(
                "Dear Provider,%nClient accepted the reschedule "
                        + "for booking ID %d.%nNew time: %s - %s",
                bookingDto.id(),
                bookingDto.startTime(),
                bookingDto.endTime()
        );
        sendEmail(to, subject, body);
    }

    @Override
    public void sendRescheduleConfirmedEmail(String to,
                                             BookingResponseDto bookingDto) {
        String subject = "Booking Reschedule Confirmed";
        String body = String.format(
                "Dear Client,%nYour booking ID %d has been rescheduled to %s - %s.",
                bookingDto.id(),
                bookingDto.startTime(),
                bookingDto.endTime()
        );
        sendEmail(to, subject, body);
    }

    @Override
    public void sendRescheduleDeclinedEmail(String to,
                                            BookingResponseDto bookingDto) {
        String subject = "Booking Reschedule Declined";
        String body = String.format(
                "Dear Provider,%nClient declined the "
                        + "reschedule proposal for booking ID %d.",
                bookingDto.id()
        );
        sendEmail(to, subject, body);
    }

    @Override
    public void sendReminderEmail(String to, BookingResponseDto bookingDto,
                                  String reminderTime) {
        String subject = "Reminder: Upcoming Appointment in " + reminderTime;
        String body = String.format(
                "Dear Client,%n"
                        + "This is a reminder that you have "
                        + "an appointment scheduled in %s.%n"
                        + "Service: %s%n"
                        + "Specialist: %s%n"
                        + "Date & Time: %s - %s%n"
                        + "Booking ID: %d%n",
                reminderTime,
                bookingDto.serviceName(),
                bookingDto.specialistName(),
                bookingDto.startTime(),
                bookingDto.endTime(),
                bookingDto.id()
        );
        sendEmail(to, subject, body);
    }

    @Override
    public void sendDailyBookingSummaryEmail(String to,
                                             List<BookingResponseDto> bookings,
                                             LocalDate date) {
        if (bookings.isEmpty()) {
            return;
        }

        StringBuilder body = new StringBuilder();
        body.append("Dear Provider,\n\nHere is your booking summary for ")
                .append(date).append(":\n\n");

        for (BookingResponseDto booking : bookings) {
            body.append("Time: ")
                    .append(booking.startTime().toLocalTime())
                    .append(" - ")
                    .append(booking.endTime().toLocalTime())
                    .append("\nClient ID: ")
                    .append(booking.clientId())
                    .append("\nService: ")
                    .append(booking.serviceName())
                    .append("\n\n");
        }

        sendEmail(to, "Your Daily Booking Summary for "
                + date, body.toString());
    }

    @Override
    public void sendFollowUpEmail(String to, BookingResponseDto bookingDto) {
        String subject = "Thank you for your service! Please leave a review or book again";

        String reviewLink = "https://yourapp.com/review/" + bookingDto.id();
        String rebookLink = "https://yourapp.com/book?specialistId="
                + bookingDto.specialistId();

        String body = String.format(
                "Your appointment with %s has been completed.%n"
                        + "Please leave a review: %s%n"
                        + "Or book again: %s%n",
                bookingDto.specialistName(),
                reviewLink,
                rebookLink
        );

        sendEmail(to, subject, body);
    }

    @Override
    public void sendPasswordResetLink(Long userId, String resetLink) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String subject = "Password Reset Request";
        String body = "Hello " + user.getFirstName() + ",\n\n"
                + "We received a request to reset your password.\n"
                + "Click the link below to set a new password (valid for 1 hour):\n"
                + resetLink + "\n\n"
                + "If you didn't request this, please ignore this email.";

        sendEmail(user.getEmail(), subject, body);
    }

    @Override
    public void sendPasswordUpdateNotification(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String subject = "Password Updated";
        String body = "Hello " + user.getFirstName() + ",\n\n"
                + "Your password was updated by an administrator.\n"
                + "If you did not request this change, please contact support immediately.";

        sendEmail(user.getEmail(), subject, body);
    }

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
