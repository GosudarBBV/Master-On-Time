package com.master.on.time.master.on.time.repository;

import com.master.on.time.master.on.time.model.PortfolioImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioImageRepository extends JpaRepository<PortfolioImage, Long> {
    List<PortfolioImage> findByUserId(Long userId);
}
