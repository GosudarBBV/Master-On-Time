package com.master.on.time.master.on.time.repository;

import com.master.on.time.master.on.time.model.Specialist;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialistRepository extends JpaRepository<Specialist, Long> {
    List<Specialist> findByStatusOrderByRegistrationDateDesc(String status);

    List<Specialist> findByStatus(String status);
}
