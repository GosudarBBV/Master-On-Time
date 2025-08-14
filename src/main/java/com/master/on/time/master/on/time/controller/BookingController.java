package com.master.on.time.master.on.time.controller;

import com.master.on.time.master.on.time.dto.BlockTimeSlotRequestDto;
import com.master.on.time.master.on.time.dto.BookingRequestDto;
import com.master.on.time.master.on.time.dto.BookingRescheduleRequestDto;
import com.master.on.time.master.on.time.dto.BookingResponseDto;
import com.master.on.time.master.on.time.service.BookingService;
import com.master.on.time.master.on.time.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Tag(name = "Booking", description = "Operations related to bookings and appointments")
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

    @GetMapping("/available-slots")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get available time slots",
            description = "Returns available booking slots for "
                    + "a specific specialist and service item on a given date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of available time slots"),
            @ApiResponse(responseCode = "404", description = "Specialist or Service not found")
    })
    public List<LocalDateTime> getAvailableSlots(
            @Parameter(description = "ID of the specialist") @RequestParam Long specialistId,
            @Parameter(description = "ID of the service item") @RequestParam Long serviceItemId,
            @Parameter(description = "Date to check availability (yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return bookingService.getAvailableTimeSlots(specialistId, serviceItemId, date);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Book a time slot",
            description = "Allows a user to book a specific time for a service from a specialist.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookingResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Time slot is already booked or invalid request")
    })
    public BookingResponseDto bookAppointment(
            @RequestBody @Valid BookingRequestDto requestDto
    ) {
        Long clientId = userService.getAuthenticatedUserId();
        return bookingService.bookTimeSlot(clientId, requestDto);
    }

    @PreAuthorize("hasRole('SPECIALIST')")
    @PostMapping("/reschedule")
    @Operation(summary = "Propose reschedule for a booking")
    public void proposeReschedule(
            @RequestBody @Valid BookingRescheduleRequestDto dto
    ) {
        Long specialistId = userService.getAuthenticatedUserId();
        bookingService.proposeReschedule(specialistId, dto);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{bookingId}/reschedule-response")
    @Operation(summary = "Respond to reschedule proposal")
    public void respondToReschedule(
            @PathVariable Long bookingId,
            @RequestBody @Valid RescheduleResponseDto responseDto
    ) {
        Long clientId = userService.getAuthenticatedUserId();
        bookingService.respondToReschedule(clientId, bookingId, responseDto.accept());
    }

    @PreAuthorize("hasAnyRole('USER','SPECIALIST')")
    @PostMapping("/{bookingId}/cancel")
    @Operation(summary = "Cancel booking")
    public void cancelBooking(@PathVariable Long bookingId) {
        Long userId = userService.getAuthenticatedUserId();
        bookingService.cancelBooking(userId, bookingId);
    }

    @PreAuthorize("hasRole('SPECIALIST')")
    @PostMapping("/block")
    @Operation(summary = "Block a time slot")
    public BookingResponseDto blockTimeSlot(@RequestBody @Valid BlockTimeSlotRequestDto request) {
        Long specialistId = userService.getAuthenticatedUserId();
        return bookingService.blockTimeSlot(
                specialistId,
                request.startTime(),
                request.endTime(),
                request.reason()
        );
    }

    @PreAuthorize("hasRole('SPECIALIST')")
    @DeleteMapping("/block/{bookingId}")
    @Operation(summary = "Unblock a time slot")
    public void unblockTimeSlot(@PathVariable Long bookingId) {
        Long specialistId = userService.getAuthenticatedUserId();
        bookingService.unblockTimeSlot(bookingId, specialistId);
    }

    @PreAuthorize("hasAnyRole('USER', 'SPECIALIST')")
    @GetMapping("/appointments/upcoming")
    @Operation(summary = "Get upcoming appointments for authenticated user")
    public List<BookingResponseDto> getUpcomingAppointments() {
        Long userId = userService.getAuthenticatedUserId();
        return bookingService.getUpcomingAppointmentsDto(userId);
    }

    @PreAuthorize("hasAnyRole('USER', 'SPECIALIST')")
    @GetMapping("/confirmed")
    @Operation(summary = "Get confirmed bookings for authenticated user")
    public List<BookingResponseDto> getConfirmedBookings() {
        Long userId = userService.getAuthenticatedUserId();
        return bookingService.getConfirmedBookingsForUser(userId);
    }

    @PreAuthorize("hasAnyRole('USER', 'SPECIALIST')")
    @PostMapping("/sync-google-calendar")
    @Operation(summary = "Sync user bookings with Google Calendar")
    public void syncBookingsWithGoogleCalendar() {
        Long userId = userService.getAuthenticatedUserId();
        bookingService.syncUserBookingsWithGoogleCalendar(userId);
    }

    @PreAuthorize("hasRole('SPECIALIST')")
    @GetMapping("/provider/{specialistId}/date/{date}")
    @Operation(summary = "Get bookings for specialist on a specific date")
    public List<BookingResponseDto> getBookingsForSpecialistOnDate(
            @PathVariable Long specialistId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return bookingService.getBookingsForSpecialistIdOnDate(specialistId, date);
    }

    @GetMapping("/{bookingId}")
    @PreAuthorize("hasAnyRole('USER', 'SPECIALIST')")
    @Operation(summary = "Get booking by ID")
    public BookingResponseDto getBookingById(@PathVariable Long bookingId) {
        Long userId = userService.getAuthenticatedUserId();
        return bookingService.getBookingById(bookingId, userId);
    }

    public static record RescheduleResponseDto(boolean accept) {}
}
