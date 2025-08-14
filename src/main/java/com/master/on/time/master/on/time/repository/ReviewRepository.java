package com.master.on.time.master.on.time.repository;

import com.master.on.time.master.on.time.model.Booking;
import com.master.on.time.master.on.time.model.Review;
import com.master.on.time.master.on.time.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findBySpecialistOrderByCreatedAtDesc(User specialist);

    long countBySpecialist(User specialist);

    Optional<Review> findByBookingId(Long bookingId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.specialist = :specialist")
    Double findAverageStarRatingBySpecialist(@Param("specialist") User specialist);

    List<Review> findAllBySpecialistIdOrderByCreatedAtDesc(Long specialistId);

    boolean existsByBooking(Booking booking);
}
