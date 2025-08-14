package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.exception.EntityNotFoundException;
import com.master.on.time.master.on.time.model.Booking;
import com.master.on.time.master.on.time.model.User;
import com.master.on.time.master.on.time.repository.BookingRepository;
import com.master.on.time.master.on.time.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.net.URI;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Transactional
@RequiredArgsConstructor
public class GoogleCalendarServiceImpl implements GoogleCalendarService {
    private static final String GOOGLE_AUTH_BASE_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String GOOGLE_CALENDAR_EVENTS_URL = "https://www.googleapis.com/calendar/v3/calendars/primary/events";

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    @Override
    public String buildAuthorizationUrl() {
        URI uri = UriComponentsBuilder.fromUriString(GOOGLE_AUTH_BASE_URL)
                .queryParam("scope", "https://www.googleapis.com/auth/calendar")
                .queryParam("access_type", "offline")
                .queryParam("include_granted_scopes", "true")
                .queryParam("response_type", "code")
                .queryParam("redirect_uri", redirectUri)
                .queryParam("client_id", clientId).build().toUri();

        return uri.toString();
    }

    @Override
    public void exchangeCodeForTokensAndSave(String code, Long userId) {
        final RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request
                = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(GOOGLE_TOKEN_URL,
                request, Map.class);

        Map<String, Object> responseBody = response.getBody();
        if (responseBody == null || !responseBody.containsKey("access_token")) {
            throw new RuntimeException("Failed to get access token from Google");
        }

        String accessToken = (String) responseBody.get("access_token");
        String refreshToken = (String) responseBody.get("refresh_token");

        User user = userRepository.findById(userId).orElseThrow(()
                -> new EntityNotFoundException("User not found with id: " + userId));

        user.setGoogleCalendarAccessToken(accessToken);
        if (refreshToken != null) {
            user.setGoogleCalendarRefreshToken(refreshToken);
        }
        user.setCalendarSyncEnabled(true);

        userRepository.save(user);
    }

    @Override
    public void syncBookingsWithGoogleCalendar(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new EntityNotFoundException("User not found with id: " + userId));

        if (!user.isCalendarSyncEnabled()) {
            throw new IllegalStateException("Calendar sync is not enabled for user with id "
                    + userId);
        }

        String accessToken = user.getGoogleCalendarAccessToken();

        List<Booking> bookings
                = bookingRepository.findByClientIdAndStatus(userId, "CONFIRMED");

        RestTemplate restTemplate = new RestTemplate();

        for (Booking booking : bookings) {
            Map<String, Object> event = new HashMap<>();
            event.put("summary", "Booking with "
                    + booking.getSpecialist().getFirstName() + " "
                    + booking.getSpecialist().getLastName());
            event.put("description", "Service: "
                    + booking.getServiceItem().getName());

            Map<String, String> start = new HashMap<>();
            start.put("dateTime", booking.getStartTime().toString());
            start.put("timeZone", ZoneId.systemDefault().toString());
            event.put("start", start);

            Map<String, String> end = new HashMap<>();
            end.put("dateTime", booking.getEndTime().toString());
            end.put("timeZone", ZoneId.systemDefault().toString());
            event.put("end", end);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(event, headers);

            try {
                restTemplate.postForEntity(GOOGLE_CALENDAR_EVENTS_URL, request, String.class);
            } catch (Exception e) {
                throw new RuntimeException("Failed to sync event to Google Calendar: "
                        + e.getMessage(), e);
            }
        }
    }

    @Override
    public void refreshAccessTokenIfNeeded(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new EntityNotFoundException("User not found with id: " + userId));

        String refreshToken = user.getGoogleCalendarRefreshToken();
        if (refreshToken == null) {
            throw new IllegalStateException("No refresh token available");
        }
        final RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("refresh_token", refreshToken);
        body.add("grant_type", "refresh_token");

        HttpEntity<MultiValueMap<String, String>> request
                = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate
                .postForEntity(GOOGLE_TOKEN_URL, request, Map.class);

        Map<String, Object> responseBody = response.getBody();
        if (responseBody == null || !responseBody.containsKey("access_token")) {
            throw new RuntimeException("Failed to refresh access token");
        }

        String newAccessToken = (String) responseBody.get("access_token");

        user.setGoogleCalendarAccessToken(newAccessToken);
        userRepository.save(user);
    }
}
