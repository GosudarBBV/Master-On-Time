package com.master.on.time.master.on.time.controller;

import com.master.on.time.master.on.time.dto.ReviewModerationDto;
import com.master.on.time.master.on.time.dto.UpdateReviewRequestDto;
import com.master.on.time.master.on.time.service.ReviewAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/reviews")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Reviews", description = "Admin operations for managing reviews")
public class ReviewAdminController {

    private final ReviewAdminService reviewAdminService;

    @GetMapping
    @Operation(summary = "Get all reviews for moderation",
            description = "Returns a list of all reviews for admin moderation")
    public List<ReviewModerationDto> getAllReviews() {
        return reviewAdminService.getAllReviews();
    }

    @PutMapping("/{id}/flag")
    @Operation(summary = "Flag a review",
            description = "Flag a review by its ID for moderation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review flagged successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReviewModerationDto.class))),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    public ReviewModerationDto flagReview(
            @Parameter(description = "ID of the review to flag", required = true)
            @PathVariable Long id) {
        return reviewAdminService.flagReview(id);
    }

    @PutMapping("/{id}/edit")
    @Operation(summary = "Edit a review",
            description = "Edit review content by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review edited successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReviewModerationDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    public ReviewModerationDto editReview(
            @Parameter(description = "ID of the review to edit", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated review data", required = true)
            @RequestBody @Valid UpdateReviewRequestDto dto) {
        return reviewAdminService.editReview(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a review",
            description = "Deletes a review by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Review deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "ID of the review to delete", required = true)
            @PathVariable Long id) {
        reviewAdminService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
