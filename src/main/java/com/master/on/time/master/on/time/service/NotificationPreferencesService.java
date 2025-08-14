package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.NotificationPreferencesDto;

public interface NotificationPreferencesService {
    NotificationPreferencesDto getPreferences(Long userId);

    void savePreferences(Long userId, NotificationPreferencesDto dto);
}
