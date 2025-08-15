package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.BookingRequestDto;
import com.master.on.time.master.on.time.dto.BookingRescheduleRequestDto;
import com.master.on.time.master.on.time.dto.BookingResponseDto;
import com.master.on.time.master.on.time.exception.EntityNotFoundException;
import com.master.on.time.master.on.time.exception.InvalidBookingOperationException;
import com.master.on.time.master.on.time.mapper.BookingMapper;
import com.master.on.time.master.on.time.model.Booking;
import com.master.on.time.master.on.time.model.CategoryItem;
import com.master.on.time.master.on.time.model.SpecialistProfile;
import com.master.on.time.master.on.time.model.User;
import com.master.on.time.master.on.time.repository.BookingRepository;
import com.master.on.time.master.on.time.repository.CategoryItemRepository;
import com.master.on.time.master.on.time.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private static final String STATUS_CONFIRMED = "CONFIRMED";
    private static final String STATUS_CANCELLED = "CANCELLED";
    private static final String STATUS_BLOCKED = "BLOCKED";
    private static final String STATUS_RESCHEDULE_REQUESTED = "RESCHEDULE_REQUESTED";

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final CategoryItemRepository categoryItemRepository;
    private final BookingMapper bookingMapper;
    private final NotificationService notificationService;
    private final EmailService emailService;
    private final GoogleCalendarService googleCalendarService;

    @Override
    public List<LocalDateTime> getAvailableTimeSlots(Long specialistUserId,
                                                     Long serviceItemId, LocalDate date) {

        CategoryItem service = categoryItemRepository.findById(serviceItemId)
                .orElseThrow(() -> new EntityNotFoundException("Service not found"));

        User specialistUser = userRepository.findById(specialistUserId)
                .orElseThrow(() -> new EntityNotFoundException("Specialist not found"));

        SpecialistProfile specialist = specialistUser.getSpecialistProfile();
        if (specialist == null) {
            throw new EntityNotFoundException("Specialist profile not found for this user");
        }

        int durationMinutes = service.getDurationMinutes();
        LocalDateTime startOfDay = date.atTime(9, 0);
        LocalDateTime endOfDay = date.atTime(18, 0);

        List<LocalDateTime> availableSlots = new ArrayList<>();
        for (LocalDateTime slotStart = startOfDay;
                !slotStart.plusMinutes(durationMinutes).isAfter(endOfDay);
                slotStart = slotStart.plusMinutes(15)) {

            LocalDateTime slotEnd = slotStart.plusMinutes(durationMinutes);
            List<Booking> conflicts = bookingRepository
                    .findConflictingBookingsBySpecialistProfileId(specialist.getId(),
                            slotStart, slotEnd);

            boolean hasActiveConflict = conflicts.stream()
                    .anyMatch(b -> !b.getStatus().equals(STATUS_CANCELLED)
                            && !b.getStatus().equals(STATUS_BLOCKED));

            if (!hasActiveConflict && slotStart.isAfter(LocalDateTime.now())) {
                availableSlots.add(slotStart);
            }
        }

        return availableSlots;
    }

    @Override
    public BookingResponseDto bookTimeSlot(Long clientId, BookingRequestDto requestDto) {
        final User client = userRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));
        final User specialistUser = userRepository.findById(requestDto.specialistId())
                .orElseThrow(() -> new EntityNotFoundException("Specialist not found"));
        SpecialistProfile specialist = specialistUser.getSpecialistProfile();

        if (specialist == null) {
            throw new EntityNotFoundException("Specialist profile not found for this user");
        }

        CategoryItem service = categoryItemRepository.findById(requestDto.serviceItemId())
                .orElseThrow(() -> new EntityNotFoundException("Service not found"));
        LocalDateTime start = requestDto.startTime();
        LocalDateTime end = start.plusMinutes(service.getDurationMinutes());

        if (start.isBefore(LocalDateTime.now())) {
            throw new InvalidBookingOperationException("Cannot book a time in the past");
        }

        boolean hasConflict = bookingRepository
                .findConflictingBookingsBySpecialistProfileId(specialist.getId(),
                        start, end)
                .stream()
                .anyMatch(b -> !b.getStatus().equals(STATUS_CANCELLED)
                        && !b.getStatus().equals(STATUS_BLOCKED));

        if (hasConflict) {
            throw new InvalidBookingOperationException("Selected time slot is not available");
        }

        Booking booking = new Booking();
        booking.setClient(client);
        booking.setSpecialist(specialist);
        booking.setServiceItem(service);
        booking.setStartTime(start);
        booking.setEndTime(end);
        booking.setStatus(STATUS_CONFIRMED);
        booking.setPriceAtBooking(specialist.getPrice());

        Booking savedBooking = bookingRepository.save(booking);
        BookingResponseDto dto = bookingMapper.toDto(savedBooking);

        emailService.sendBookingConfirmationEmail(client.getEmail(), dto);
        notificationService.createBookingNotification(specialistUser.getId(), dto);

        return dto;
    }

    @Override
    public void cancelBooking(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        if (!booking.getClient().getId().equals(userId)
                && !booking.getSpecialist().getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You can only cancel your own bookings");
        }

        if (STATUS_CANCELLED.equals(booking.getStatus())) {
            throw new InvalidBookingOperationException("Booking already cancelled");
        }

        booking.setStatus(STATUS_CANCELLED);
        bookingRepository.save(booking);

        BookingResponseDto dto = bookingMapper.toDto(booking);
        notificationService.createCancellationNotification(dto.id(), dto);
        emailService.sendBookingCancellationEmail(booking.getClient().getEmail(), dto);
    }

    @Override
    public void proposeReschedule(Long specialistProfileId, BookingRescheduleRequestDto dto) {
        Booking booking = bookingRepository.findById(dto.bookingId())
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        if (!booking.getSpecialist().getId().equals(specialistProfileId)) {
            throw new AccessDeniedException("Only specialist can propose reschedule");
        }

        if (!STATUS_CONFIRMED.equals(booking.getStatus())) {
            throw new InvalidBookingOperationException("Only confirmed "
                    + "bookings can be rescheduled");
        }

        LocalDateTime newStart = dto.proposedStartTime();
        LocalDateTime newEnd = dto.proposedEndTime();

        if (newStart.isBefore(LocalDateTime.now())) {
            throw new InvalidBookingOperationException("Cannot reschedule to past time");
        }

        boolean hasConflict = bookingRepository.findConflictingBookings(specialistProfileId,
                        newStart, newEnd)
                .stream().anyMatch(b -> !b.getId().equals(booking.getId())
                        && !b.getStatus().equals(STATUS_CANCELLED));

        if (hasConflict) {
            throw new InvalidBookingOperationException("New time "
                    + "slot conflicts with existing bookings");
        }

        booking.setStatus(STATUS_RESCHEDULE_REQUESTED);
        booking.setProposedStartTime(newStart);
        booking.setProposedEndTime(newEnd);
        booking.setRescheduleMessage(dto.message());
        bookingRepository.save(booking);

        BookingResponseDto dtoResponse = bookingMapper.toDto(booking);
        notificationService
                .createRescheduleProposalNotification(booking.getClient().getId(),
                        dtoResponse);
        emailService.sendRescheduleProposalEmail(booking.getClient().getEmail(),
                dtoResponse);
    }

    @Override
    public void respondToReschedule(Long clientId, Long bookingId, boolean accept) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        if (!booking.getClient().getId().equals(clientId)) {
            throw new AccessDeniedException("Only client can "
                    + "respond to reschedule request");
        }

        if (!STATUS_RESCHEDULE_REQUESTED.equals(booking.getStatus())) {
            throw new InvalidBookingOperationException("No reschedule request pending");
        }

        if (accept) {
            booking.setStartTime(booking.getProposedStartTime());
            booking.setEndTime(booking.getProposedEndTime());
            booking.setStatus(STATUS_CONFIRMED);
        }

        booking.setProposedStartTime(null);
        booking.setProposedEndTime(null);
        booking.setRescheduleMessage(null);
        bookingRepository.save(booking);

        BookingResponseDto dto = bookingMapper.toDto(booking);
        if (accept) {
            notificationService.createRescheduleAcceptedNotification(booking
                            .getSpecialist().getUser().getId(),
                    dto);
            emailService.sendRescheduleAcceptedEmail(booking
                            .getSpecialist().getUser().getEmail(),
                    dto);
        } else {
            notificationService.createRescheduleDeclinedNotification(booking
                            .getSpecialist().getUser().getId(),
                    dto);
            emailService.sendRescheduleDeclinedEmail(booking
                            .getSpecialist().getUser().getEmail(),
                    dto);
        }
    }

    @Override
    public BookingResponseDto blockTimeSlot(Long specialistUserId, LocalDateTime startTime,
                                            LocalDateTime endTime, String reason) {
        // Отримуємо користувача
        User specialistUser = userRepository.findById(specialistUserId)
                .orElseThrow(() -> new EntityNotFoundException("Specialist not found"));

        // Беремо профіль спеціаліста
        SpecialistProfile specialist = specialistUser.getSpecialistProfile();
        if (specialist == null) {
            throw new EntityNotFoundException("Specialist profile not found");
        }

        if (startTime.isBefore(LocalDateTime.now())) {
            throw new InvalidBookingOperationException("Cannot block past time slots");
        }

        boolean hasConflict = bookingRepository.findConflictingBookings(specialist
                        .getId(), startTime, endTime)
                .stream().anyMatch(b -> !b.getStatus().equals(STATUS_CANCELLED)
                        && !b.getStatus().equals(STATUS_BLOCKED));

        if (hasConflict) {
            throw new InvalidBookingOperationException("Time slot "
                    + "conflicts with existing bookings");
        }

        Booking block = new Booking();
        block.setSpecialist(specialist);
        block.setStartTime(startTime);
        block.setEndTime(endTime);
        block.setStatus(STATUS_BLOCKED);
        block.setReason(reason);
        block.setPriceAtBooking(specialist.getPrice());

        Booking savedBlock = bookingRepository.save(block);
        return bookingMapper.toDto(savedBlock);
    }

    @Override
    public void unblockTimeSlot(Long bookingId, Long specialistProfileId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        if (!booking.getSpecialist().getId().equals(specialistProfileId)) {
            throw new AccessDeniedException("Only specialist can unblock own blocked slots");
        }

        if (!STATUS_BLOCKED.equals(booking.getStatus())) {
            throw new InvalidBookingOperationException("Booking is not a blocked slot");
        }

        bookingRepository.delete(booking);
    }

    @Override
    public List<BookingResponseDto> getConfirmedBookingsForUser(Long userId) {
        List<Booking> bookings = bookingRepository.findByClientIdAndStatus(userId,
                STATUS_CONFIRMED);
        return bookings.stream().map(bookingMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void syncUserBookingsWithGoogleCalendar(Long userId) {
        userRepository.findById(userId).orElseThrow(()
                -> new EntityNotFoundException("User not found"));
        googleCalendarService.syncBookingsWithGoogleCalendar(userId);
    }

    @Override
    public List<BookingResponseDto> getUpcomingAppointments(Long userId) {
        List<Booking> upcoming = bookingRepository.findUpcomingAppointments(userId);
        return upcoming.stream().map(bookingMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getUpcomingAppointmentsDto(Long userId) {
        return getUpcomingAppointments(userId);
    }

    @Override
    public List<BookingResponseDto> getBookingsForSpecialistIdOnDate(Long specialistProfileId,
                                                                     LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay().minusSeconds(1);

        List<Booking> bookings = bookingRepository.findBySpecialistIdAndStartTimeBetweenAndStatus(
                specialistProfileId, startOfDay, endOfDay, STATUS_CONFIRMED);

        return bookings.stream().map(bookingMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public BookingResponseDto getBookingById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        if (!booking.getClient().getId().equals(userId)
                && !booking.getSpecialist().getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You do not have access to this booking");
        }

        return bookingMapper.toDto(booking);
    }
}
