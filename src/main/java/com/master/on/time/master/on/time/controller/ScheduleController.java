package com.master.on.time.master.on.time.controller;

import com.master.on.time.master.on.time.dto.AvailabilityRequestDto;
import com.master.on.time.master.on.time.dto.UnavailabilityRequestDto;
import com.master.on.time.master.on.time.service.ScheduleService;
import com.master.on.time.master.on.time.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
@Tag(name = "Schedule", description = "Manage specialist availability and unavailability")
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final UserService userService;

    @Operation(summary = "Set working days and hours availability")
    @PreAuthorize("hasRole('SPECIALIST')")
    @PutMapping("/availability")
    public void setAvailability(
            @Parameter(description = "List of availability per day", required = true)
            @RequestBody @Valid List<AvailabilityRequestDto> availabilityList) {
        Long specialistId = getAuthenticatedSpecialistId();
        scheduleService.setAvailability(specialistId, availabilityList);
    }

    @Operation(summary = "Add a new unavailability time block (breaks, holidays)")
    @PreAuthorize("hasRole('SPECIALIST')")
    @PostMapping("/unavailability")
    public void addUnavailability(
            @Parameter(description = "Unavailability time interval", required = true)
            @RequestBody @Valid UnavailabilityRequestDto unavailabilityDto) {
        Long specialistId = getAuthenticatedSpecialistId();
        scheduleService.addUnavailability(specialistId, unavailabilityDto);
    }

    @Operation(summary = "Get specialist's current availability schedule")
    @PreAuthorize("hasRole('SPECIALIST')")
    @GetMapping("/availability")
    public List<AvailabilityRequestDto> getAvailability() {
        Long specialistId = getAuthenticatedSpecialistId();
        return scheduleService.getAvailability(specialistId);
    }

    @Operation(summary = "Get specialist's unavailability time blocks")
    @PreAuthorize("hasRole('SPECIALIST')")
    @GetMapping("/unavailability")
    public List<UnavailabilityRequestDto> getUnavailabilities() {
        Long specialistId = getAuthenticatedSpecialistId();
        return scheduleService.getUnavailabilities(specialistId);
    }

    private Long getAuthenticatedSpecialistId() {
        Long userId = userService.getAuthenticatedUserId();
        if (userId == null) {
            throw new IllegalStateException("User is not authenticated");
        }
        return userId;
    }
}
