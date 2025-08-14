package com.master.on.time.master.on.time.controller;

import com.master.on.time.master.on.time.service.AdminPasswordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/password")
@RequiredArgsConstructor
@Tag(name = "Admin: Password Management",
        description = "Endpoints for generating "
                + "reset links and manually updating user passwords")
public class AdminPasswordController {

    private final AdminPasswordService passwordService;

    @PostMapping("/{userId}/reset-link")
    @Operation(
            summary = "Generate password reset link",
            description = "Creates a temporary password reset link for a specific user"
    )
    public String generateResetLink(
            @Parameter(description = "ID of the user") @PathVariable Long userId
    ) {
        return passwordService.generatePasswordResetLink(userId);
    }

    @PostMapping("/{userId}/update")
    @Operation(
            summary = "Manually update user password",
            description = "Updates the password for a specific user without sending a reset link"
    )
    public void updatePasswordManually(
            @Parameter(description = "ID of the user") @PathVariable Long userId,
            @Parameter(description = "New password for the user") @RequestParam String newPassword
    ) {
        passwordService.updatePasswordManually(userId, newPassword);
    }
}
