package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.ReviewModerationRequestDto;
import com.master.on.time.master.on.time.dto.ReviewModerationResponseDto;
import com.master.on.time.master.on.time.dto.ReviewRequestDto;
import com.master.on.time.master.on.time.dto.ReviewResponseDto;
import com.master.on.time.master.on.time.exception.EntityNotFoundException;
import com.master.on.time.master.on.time.exception.IllegalBookingStateException;
import com.master.on.time.master.on.time.exception.ReviewAlreadyExistsException;
import com.master.on.time.master.on.time.exception.UnauthorizedActionException;
import com.master.on.time.master.on.time.mapper.ReviewMapper;
import com.master.on.time.master.on.time.model.Booking;
import com.master.on.time.master.on.time.model.Review;
import com.master.on.time.master.on.time.model.ReviewStatus;
import com.master.on.time.master.on.time.model.User;
import com.master.on.time.master.on.time.repository.BookingRepository;
import com.master.on.time.master.on.time.repository.ReviewRepository;
import com.master.on.time.master.on.time.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ReviewMapper reviewMapper;
    private final UserRepository userRepository;

    @Override
    public ReviewResponseDto addReview(ReviewRequestDto dto) {
        Long currentUserId = userService.getAuthenticatedUserId();

        Booking booking = bookingRepository.findById(dto.bookingId())
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        validateBookingOwnership(booking, currentUserId);
        validateBookingCompleted(booking);
        validateReviewNotExists(booking);

        User client = userRepository.findById(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Review review = new Review();
        review.setRating(dto.rating());
        review.setComment(dto.comment());
        review.setBooking(booking);
        review.setClient(client);
        review.setSpecialist(booking.getSpecialist());
        review.setStatus(ReviewStatus.VISIBLE);
        review.setCreatedAt(LocalDateTime.now());

        Review savedReview = reviewRepository.save(review);

        return reviewMapper.toDto(savedReview);
    }

    @Override
    public List<ReviewResponseDto> getReviewsForSpecialist(Long specialistId) {
        List<Review> reviews = reviewRepository
                .findAllBySpecialistIdOrderByCreatedAtDesc(specialistId);
        return reviews.stream()
                .map(reviewMapper::toDto)
                .toList();
    }

    @Override
    public ReviewResponseDto updateReview(Long reviewId, ReviewRequestDto dto) {
        Long currentUserId = userService.getAuthenticatedUserId();
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        validateReviewOwnership(review, currentUserId);

        review.setRating(dto.rating());
        review.setComment(dto.comment());

        Review updated = reviewRepository.save(review);

        return reviewMapper.toDto(updated);
    }

    @Override
    public void deleteReview(Long reviewId) {
        Long currentUserId = userService.getAuthenticatedUserId();
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        validateReviewOwnership(review, currentUserId);

        reviewRepository.delete(review);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<ReviewModerationResponseDto> getAllReviewsForModeration() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream()
                .map(reviewMapper::toModerationDto)
                .toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ReviewModerationResponseDto moderateReview(Long reviewId,
                                                      ReviewModerationRequestDto dto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        review.setStatus(dto.status());
        reviewRepository.save(review);

        return reviewMapper.toModerationDto(review);
    }

    @Override
    public boolean canReview(Long bookingId) {
        Long currentUserId = userService.getAuthenticatedUserId();

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        return "COMPLETED".equalsIgnoreCase(booking.getStatus())
                && booking.getClient().getId().equals(currentUserId)
                && !reviewRepository.existsByBooking(booking);
    }

    @Override
    public ReviewResponseDto submitReview(Long bookingId, Long userId, ReviewRequestDto dto) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        validateBookingOwnership(booking, userId);
        validateBookingCompleted(booking);
        validateReviewNotExists(booking);

        Review review = new Review();
        review.setBooking(booking);
        review.setClient(booking.getClient());
        review.setSpecialist(booking.getSpecialist());
        review.setRating(dto.rating());
        review.setComment(dto.comment());
        review.setCreatedAt(LocalDateTime.now());

        Review saved = reviewRepository.save(review);
        return reviewMapper.toDto(saved);
    }

    private void validateBookingOwnership(Booking booking, Long userId) {
        if (!booking.getClient().getId().equals(userId)) {
            throw new UnauthorizedActionException("You can only review your own bookings.");
        }
    }

    private void validateBookingCompleted(Booking booking) {
        if (!"COMPLETED".equalsIgnoreCase(booking.getStatus())) {
            throw new IllegalBookingStateException("You can only leave"
                    + " a review after completing the service.");
        }
    }

    private void validateReviewNotExists(Booking booking) {
        if (reviewRepository.existsByBooking(booking)) {
            throw new ReviewAlreadyExistsException("You have already reviewed this booking.");
        }
    }

    private void validateReviewOwnership(Review review, Long userId) {
        if (!review.getClient().getId().equals(userId)) {
            throw new AccessDeniedException("You can only modify your own reviews");
        }
    }
}
