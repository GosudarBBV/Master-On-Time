package com.master.on.time.master.on.time.service;

public interface AdminPasswordService {
    String generatePasswordResetLink(Long userId);

    void updatePasswordManually(Long userId, String newPassword);
}
