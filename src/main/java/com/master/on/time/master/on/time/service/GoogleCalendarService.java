package com.master.on.time.master.on.time.service;

public interface GoogleCalendarService {
    String buildAuthorizationUrl();

    void exchangeCodeForTokensAndSave(String code, Long userId);

    void syncBookingsWithGoogleCalendar(Long userId);

    void refreshAccessTokenIfNeeded(Long userId);
}
