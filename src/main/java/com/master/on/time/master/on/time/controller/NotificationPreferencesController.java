package com.master.on.time.master.on.time.controller;

import com.master.on.time.master.on.time.dto.NotificationPreferencesDto;
import com.master.on.time.master.on.time.service.NotificationPreferencesService;
import com.master.on.time.master.on.time.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notification-preferences")
@RequiredArgsConstructor
@Tag(name = "Notification Preferences", description = "Manage user notification preferences")
public class NotificationPreferencesController {

    private final NotificationPreferencesService preferencesService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get notification preferences for authenticated user")
    @GetMapping
    public NotificationPreferencesDto getPreferences() {
        Long userId = userService.getAuthenticatedUserId();
        return preferencesService.getPreferences(userId);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update notification preferences for authenticated user")
    @PutMapping
    public void updatePreferences(@RequestBody @Valid NotificationPreferencesDto dto) {
        Long userId = userService.getAuthenticatedUserId();
        preferencesService.savePreferences(userId, dto);
    }
}
