package com.master.on.time.master.on.time.controller;

import com.master.on.time.master.on.time.dto.UserDetailsDto;
import com.master.on.time.master.on.time.dto.UserDto;
import com.master.on.time.master.on.time.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Tag(name = "Admin: User Management", description = "Endpoints for searching,"
        + " viewing, and managing users")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/search")
    @Operation(
            summary = "Search users",
            description = "Search users by username, email, user type, and status"
    )
    public List<UserDto> searchUsers(
            @Parameter(description = "Filter by username")
            @RequestParam(required = false)
            String username,
            @Parameter(description = "Filter by email")
            @RequestParam(required = false) String email,
            @Parameter(description = "Filter by user type (USER, SPECIALIST, ADMIN)")
            @RequestParam(required = false)
            String userType,
            @Parameter(description = "Filter by account status (ACTIVE, INACTIVE, FLAGGED)")
            @RequestParam(required = false) String status
    ) {
        return adminService.searchUsers(username, email, userType, status);
    }

    @GetMapping("/{userId}")
    @Operation(
            summary = "Get user details",
            description = "Retrieve full details for a specific user"
    )
    public UserDetailsDto getUserDetails(
            @Parameter(description = "ID of the user") @PathVariable Long userId
    ) {
        return adminService.getUserDetails(userId);
    }

    @PutMapping("/{userId}/deactivate")
    @Operation(
            summary = "Deactivate user",
            description = "Deactivate the account of a specific user"
    )
    public void deactivateUser(
            @Parameter(description = "ID of the user to deactivate") @PathVariable Long userId
    ) {
        adminService.deactivateUser(userId);
    }

    @PutMapping("/{userId}/reactivate")
    @Operation(
            summary = "Reactivate user",
            description = "Reactivate the account of a specific user"
    )
    public void reactivateUser(
            @Parameter(description = "ID of the user to reactivate") @PathVariable Long userId
    ) {
        adminService.reactivateUser(userId);
    }

    @PutMapping("/{userId}/flag")
    @Operation(
            summary = "Flag user",
            description = "Mark a user account as flagged for review"
    )
    public void flagUser(
            @Parameter(description = "ID of the user to flag") @PathVariable Long userId
    ) {
        adminService.flagUser(userId);
    }
}
