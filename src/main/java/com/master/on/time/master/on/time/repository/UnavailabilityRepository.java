package com.master.on.time.master.on.time.repository;

import com.master.on.time.master.on.time.model.Unavailability;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnavailabilityRepository extends JpaRepository<Unavailability, Long> {
    List<Unavailability> findBySpecialistId(Long specialistId);
}
