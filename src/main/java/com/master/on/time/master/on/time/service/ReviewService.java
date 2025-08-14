package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.ReviewModerationRequestDto;
import com.master.on.time.master.on.time.dto.ReviewModerationResponseDto;
import com.master.on.time.master.on.time.dto.ReviewRequestDto;
import com.master.on.time.master.on.time.dto.ReviewResponseDto;
import java.util.List;

public interface ReviewService {
    ReviewResponseDto submitReview(Long bookingId, Long userId, ReviewRequestDto dto);

    List<ReviewResponseDto> getReviewsForSpecialist(Long specialistId);

    ReviewResponseDto updateReview(Long reviewId, ReviewRequestDto dto);

    void deleteReview(Long reviewId);

    List<ReviewModerationResponseDto> getAllReviewsForModeration();

    ReviewModerationResponseDto moderateReview(Long reviewId,
                                               ReviewModerationRequestDto dto);

    ReviewResponseDto addReview(ReviewRequestDto dto);

    boolean canReview(Long bookingId);
}
