package com.master.on.time.master.on.time.repository;

import com.master.on.time.master.on.time.model.NotificationPreferences;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationPreferencesRepository
        extends JpaRepository<NotificationPreferences, Long> {
    Optional<NotificationPreferences> findByUserId(Long userId);
}
