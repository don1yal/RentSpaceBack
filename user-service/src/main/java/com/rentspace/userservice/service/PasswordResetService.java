package com.rentspace.userservice.service;

import org.springframework.stereotype.Service;

@Service
public interface PasswordResetService {
    public void sendResetCode(String email);
    public void resetPassword(String email, String resetCode, String newPassword);
}