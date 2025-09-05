package com.master.on.time.master.on.time.controller;

import com.master.on.time.master.on.time.dto.UserProfileUpdateRequestDto;
import com.master.on.time.master.on.time.dto.UserResponseDto;
import com.master.on.time.master.on.time.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Endpoints for managing users (clients & specialists)")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    @Operation(
            summary = "Get current user profile",
            description = "Returns the authenticated user's profile",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved user profile"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasAnyRole('USER', 'SPECIALIST', 'ADMIN')")
    public UserResponseDto getProfile() {
        Long userId = userService.getAuthenticatedUserId();
        return userService.getUserById(userId);
    }

    @Operation(summary = "Get all specialists")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "List of specialists returned successfully"),
            @ApiResponse(responseCode = "404", description = "No specialists found")
    })
    @GetMapping("/specialists")
    public List<UserResponseDto> getAllSpecialists() {
        return userService.getAllSpecialists();
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete user by ID",
            description = "Only admins can delete users by ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @Parameter(description = "ID of the user to delete", required = true)
            @PathVariable Long id) {
        userService.deleteById(id);
    }

    @PutMapping("/me")
    @Operation(
            summary = "Update current user profile",
            description = "Clients and specialists can update their personal information"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasAnyRole('USER', 'CLIENT', 'SPECIALIST')")
    public UserResponseDto updateProfile(
            @Parameter(description = "User profile update data", required = true)
            @RequestBody @Valid UserProfileUpdateRequestDto dto) {
        Long userId = userService.getAuthenticatedUserId();
        return userService.updateUserProfile(userId, dto);
    }

    @PatchMapping("/me/visibility")
    @Operation(
            summary = "Update specialist visibility status",
            description = "Allows a specialist to hide or show their public profile"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Visibility status updated"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('SPECIALIST')")
    public UserResponseDto updateVisibility(
            @Parameter(name = "visible",
                    description = "true = visible/public, false = hidden/inactive",
                    required = true)
            @RequestParam boolean visible) {
        Long userId = userService.getAuthenticatedUserId();
        return userService.updateVisibilityStatus(userId, visible);
    }

    @DeleteMapping("/me")
    @PreAuthorize("hasAnyRole('USER', 'SPECIALIST')")
    @Operation(
            summary = "Delete current user account",
            description = "Allows an authenticated user "
                    + "(client or specialist) to delete their own account"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Account deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMyAccount() {
        Long userId = userService.getAuthenticatedUserId();
        userService.deleteAccount(userId);
    }
}
