package com.master.on.time.master.on.time.controller;

import com.master.on.time.master.on.time.dto.ReviewModerationRequestDto;
import com.master.on.time.master.on.time.dto.ReviewModerationResponseDto;
import com.master.on.time.master.on.time.dto.ReviewRequestDto;
import com.master.on.time.master.on.time.dto.ReviewResponseDto;
import com.master.on.time.master.on.time.service.ReviewService;
import com.master.on.time.master.on.time.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
@Tag(name = "Reviews", description = "Endpoints for creating, managing and moderating reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    @Operation(summary = "Leave a review for a booking",
            description = "Submit a review for a completed booking by the authenticated user")
    @PostMapping("/booking/{bookingId}")
    @PreAuthorize("hasRole('USER')")
    public ReviewResponseDto leaveReview(
            @Parameter(description = "ID of the booking") @PathVariable Long bookingId,
            @RequestBody @Valid ReviewRequestDto dto
    ) {
        Long userId = userService.getAuthenticatedUserId();
        return reviewService.submitReview(bookingId, userId, dto);
    }

    @Operation(summary = "Get reviews for authenticated specialist",
            description = "Return reviews left for the authenticated specialist")
    @GetMapping("/specialist")
    @PreAuthorize("hasRole('SPECIALIST')")
    public List<ReviewResponseDto> getReviewsForSpecialist() {
        Long specialistId = userService.getAuthenticatedUserId();
        return reviewService.getReviewsForSpecialist(specialistId);
    }

    @Operation(summary = "Update a review",
            description = "Update a review by its author (allowed for USER or SPECIALIST)")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'SPECIALIST')")
    public ReviewResponseDto updateReview(
            @Parameter(description = "ID of the review to update") @PathVariable Long id,
            @RequestBody @Valid ReviewRequestDto reviewRequest
    ) {
        return reviewService.updateReview(id, reviewRequest);
    }

    @Operation(summary = "Delete a review",
            description = "Delete a review by its author (allowed for USER or SPECIALIST)")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'SPECIALIST')")
    public void deleteReview(
            @Parameter(description = "ID of the review to delete") @PathVariable Long id
    ) {
        reviewService.deleteReview(id);
    }

    @Operation(summary = "Get reviews for moderation",
            description = "Get all reviews pending moderation (admin only)")
    @GetMapping("/moderation")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ReviewModerationResponseDto> getAllForModeration() {
        return reviewService.getAllReviewsForModeration();
    }

    @Operation(summary = "Moderate a review",
            description = "Approve or reject a review as an admin")
    @PutMapping("/{id}/moderate")
    @PreAuthorize("hasRole('ADMIN')")
    public ReviewModerationResponseDto moderateReview(
            @Parameter(description = "ID of the review to moderate") @PathVariable Long id,
            @RequestBody ReviewModerationRequestDto dto
    ) {
        return reviewService.moderateReview(id, dto);
    }

    @Operation(summary = "Add a review",
            description = "Add a review (alternative endpoint, requires authenticated USER)")
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ReviewResponseDto addReview(@RequestBody @Valid ReviewRequestDto dto) {
        return reviewService.addReview(dto);
    }

    @Operation(summary = "Check if user can leave a review for booking",
            description = "Returns true if authenticated "
                    + "user is allowed to leave a review for the booking")
    @GetMapping("/can-review/{bookingId}")
    @PreAuthorize("hasAnyRole('USER', 'SPECIALIST')")
    public boolean canReview(
            @Parameter(description = "ID of the booking to check") @PathVariable Long bookingId
    ) {
        return reviewService.canReview(bookingId);
    }
}
