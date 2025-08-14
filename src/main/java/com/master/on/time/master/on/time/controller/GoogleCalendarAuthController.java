package com.master.on.time.master.on.time.controller;

import com.master.on.time.master.on.time.service.GoogleCalendarService;
import com.master.on.time.master.on.time.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@Tag(name = "Google Calendar Integration",
        description = "Endpoints for Google Calendar OAuth and sync")
public class GoogleCalendarAuthController {

    private final GoogleCalendarService googleCalendarService;
    private final UserService userService;

    @Operation(summary = "Start Google Calendar OAuth flow")
    @GetMapping("/connect/google")
    public RedirectView connectGoogleCalendar() {
        String authUrl = googleCalendarService.buildAuthorizationUrl();
        return new RedirectView(authUrl);
    }

    @Operation(summary = "Handle Google OAuth callback")
    @GetMapping("/connect/google/callback")
    public void googleCallback(@RequestParam("code") String code) {
        Long userId = userService.getAuthenticatedUserId();
        googleCalendarService.exchangeCodeForTokensAndSave(code, userId);
    }

    @PreAuthorize("hasAnyRole('USER', 'SPECIALIST')")
    @Operation(summary = "Sync bookings with Google Calendar")
    @PostMapping("/sync")
    public String syncWithGoogleCalendar() {
        Long userId = userService.getAuthenticatedUserId();
        googleCalendarService.syncBookingsWithGoogleCalendar(userId);
        return "Bookings synced with Google Calendar!";
    }
}
