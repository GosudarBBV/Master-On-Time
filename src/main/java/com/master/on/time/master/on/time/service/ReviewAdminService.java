package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.ReviewModerationDto;
import com.master.on.time.master.on.time.dto.UpdateReviewRequestDto;
import java.util.List;

public interface ReviewAdminService {
    List<ReviewModerationDto> getAllReviews();

    ReviewModerationDto flagReview(Long reviewId);

    ReviewModerationDto editReview(Long reviewId, UpdateReviewRequestDto dto);

    void deleteReview(Long reviewId);
}
