package com.master.on.time.master.on.time.controller;

import com.master.on.time.master.on.time.service.ReminderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reminders")
@RequiredArgsConstructor
@Tag(name = "Reminders", description = "Endpoints to send reminders and summaries")
public class ReminderController {

    private final ReminderService reminderService;

    @Operation(summary = "Send reminders")
    @PostMapping("/send-reminders")
    public String sendReminders() {
        reminderService.sendReminders();
        return "Reminders sent successfully.";
    }

    @Operation(summary = "Send daily booking summaries")
    @PostMapping("/send-daily-summary")
    public String sendDailySummary() {
        reminderService.sendDailySummaries();
        return "Daily booking summaries sent successfully.";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Send follow-up messages for bookings")
    @PostMapping("/send-follow-ups")
    public String sendFollowUpMessages() {
        int count = reminderService.sendFollowUpMessagesAndReturnCount();
        return "Sent follow-up messages for " + count + " bookings.";
    }
}
