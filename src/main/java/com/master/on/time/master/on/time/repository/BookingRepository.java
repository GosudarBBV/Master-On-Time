package com.master.on.time.master.on.time.repository;

import com.master.on.time.master.on.time.model.Booking;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("select b from Booking b where b.specialist.id = :specialistId "
            + "and b.status IN ('CONFIRMED', 'BLOCKED') "
            + "and ((b.startTime < :endTime) and (b.endTime > :startTime))")
    List<Booking> findConflictingBookings(@Param("specialistId") Long specialistId,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);

    @Query("SELECT b FROM Booking b WHERE b.status = 'CONFIRMED' "
            + "AND b.startTime BETWEEN :start AND :end")
    List<Booking> findConfirmedBookingsBetween(@Param("start") LocalDateTime start,
                                               @Param("end") LocalDateTime end);

    List<Booking> findByClientIdAndStatus(Long clientId, String status);

    @Query("SELECT b FROM Booking b "
            + "WHERE (b.client.id = :userId OR b.specialist.id = :userId) "
            + "AND b.startTime >= CURRENT_TIMESTAMP "
            + "AND b.status = 'CONFIRMED' "
            + "ORDER BY b.startTime ASC")
    List<Booking> findUpcomingAppointments(@Param("userId") Long userId);

    List<Booking> findBySpecialistIdAndStartTimeBetweenAndStatus(Long specialistId,
                                                                 LocalDateTime start,
                                                                 LocalDateTime end,
                                                                 String status);

    @Query("""
    SELECT b FROM Booking b
    WHERE b.followUpSent = false
    AND b.endTime BETWEEN :from AND :to""")
    List<Booking> findCompletedBookingsWithoutFollowUp(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}
