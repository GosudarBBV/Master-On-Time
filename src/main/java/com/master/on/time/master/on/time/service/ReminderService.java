package com.master.on.time.master.on.time.service;

public interface ReminderService {
    void sendReminders();

    void sendDailySummaries();

    void sendFollowUpMessages();

    int sendFollowUpMessagesAndReturnCount();
}
