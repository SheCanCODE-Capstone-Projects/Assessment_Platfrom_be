package com.talentprobe.assessment.email;

public interface EmailService {
    void sendInvitation(String email, String name, String link);
    void sendReminder(String email, String name, String link);
    void sendResult(String email, String name, String link);
}