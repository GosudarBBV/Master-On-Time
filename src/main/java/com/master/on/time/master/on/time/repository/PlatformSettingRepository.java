package com.master.on.time.master.on.time.repository;

import com.master.on.time.master.on.time.model.PlatformSetting;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformSettingRepository
        extends JpaRepository<PlatformSetting, Long> {
    Optional<PlatformSetting> findByKeyName(String keyName);
}
