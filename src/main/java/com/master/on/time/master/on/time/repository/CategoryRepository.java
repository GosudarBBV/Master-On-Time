package com.master.on.time.master.on.time.repository;

import com.master.on.time.master.on.time.model.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findBySpecialistId(Long specialistId);

    boolean existsByName(String name);
}
