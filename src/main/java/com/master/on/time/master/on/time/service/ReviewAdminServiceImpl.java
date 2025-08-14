package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.ReviewModerationDto;
import com.master.on.time.master.on.time.dto.UpdateReviewRequestDto;
import com.master.on.time.master.on.time.exception.EntityNotFoundException;
import com.master.on.time.master.on.time.mapper.ReviewMapper;
import com.master.on.time.master.on.time.model.Review;
import com.master.on.time.master.on.time.model.ReviewStatus;
import com.master.on.time.master.on.time.repository.ReviewRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewAdminServiceImpl implements ReviewAdminService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public List<ReviewModerationDto> getAllReviews() {
        return reviewRepository.findAll()
                .stream()
                .map(reviewMapper::toReviewModerationDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewModerationDto flagReview(Long reviewId) {
        Review review = getReviewOrThrow(reviewId);
        review.setStatus(ReviewStatus.FLAGGED);
        return saveAndMap(review);
    }

    @Override
    public ReviewModerationDto editReview(Long reviewId,
                                          UpdateReviewRequestDto dto) {
        Review review = getReviewOrThrow(reviewId);
        review.setComment(dto.text());
        review.setStatus(ReviewStatus.EDITED);
        return saveAndMap(review);
    }

    @Override
    public void deleteReview(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new EntityNotFoundException("Review not found");
        }
        reviewRepository.deleteById(reviewId);
    }

    private Review getReviewOrThrow(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(()
                        -> new EntityNotFoundException("Review not found"));
    }

    private ReviewModerationDto saveAndMap(Review review) {
        Review savedReview = reviewRepository.save(review);
        return reviewMapper.toReviewModerationDto(savedReview);
    }
}
