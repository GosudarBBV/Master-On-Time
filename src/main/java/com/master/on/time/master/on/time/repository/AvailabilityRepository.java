package com.master.on.time.master.on.time.repository;

import com.master.on.time.master.on.time.model.Availability;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    List<Availability> findBySpecialistId(Long specialistId);

    void deleteBySpecialistId(Long specialistId);
}
