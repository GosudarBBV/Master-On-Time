package com.master.on.time.master.on.time.repository;

import com.master.on.time.master.on.time.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsByName(String name);
}
