package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.NotificationPreferencesDto;
import com.master.on.time.master.on.time.model.NotificationPreferences;
import com.master.on.time.master.on.time.model.User;
import com.master.on.time.master.on.time.repository.NotificationPreferencesRepository;
import com.master.on.time.master.on.time.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationPreferencesServiceImpl implements NotificationPreferencesService {

    private final NotificationPreferencesRepository preferencesRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public NotificationPreferencesDto getPreferences(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(()
                        -> new IllegalArgumentException("User not found with id: "
                        + userId));

        NotificationPreferences preferences = preferencesRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultPreferences(userId));

        return mapToDto(preferences);
    }

    @Override
    public void savePreferences(Long userId, NotificationPreferencesDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(()
                        -> new IllegalArgumentException("User not found with id: "
                        + userId));

        NotificationPreferences preferences = preferencesRepository.findByUserId(userId)
                .orElseGet(() -> {
                    NotificationPreferences np = new NotificationPreferences();
                    np.setUser(user);
                    return np;
                });

        preferences.setEmailEnabled(dto.emailEnabled());
        preferences.setSmsEnabled(dto.smsEnabled());
        preferences.setInAppEnabled(dto.inAppEnabled());

        preferencesRepository.save(preferences);
    }

    private NotificationPreferences createDefaultPreferences(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()
                        -> new IllegalArgumentException("User not found with id: "
                        + userId));

        NotificationPreferences np = new NotificationPreferences();
        np.setUser(user);
        np.setEmailEnabled(true);
        np.setSmsEnabled(false);
        np.setInAppEnabled(true);

        return preferencesRepository.save(np);
    }

    private NotificationPreferencesDto mapToDto(NotificationPreferences preferences) {
        return new NotificationPreferencesDto(
                preferences.isEmailEnabled(),
                preferences.isSmsEnabled(),
                preferences.isInAppEnabled()
        );
    }
}
