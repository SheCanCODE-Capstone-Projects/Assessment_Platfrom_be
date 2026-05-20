package com.talentprobe.assessment.email;

public interface EmailService {

    void sendInvitation(String email, String name, String link);
    void sendReminder(String email, String name, String link);
    void sendResult(String email, String name, String link);

    void sendInvitation(String email, String name, String link, String startTime, String endTime);
    void sendReminder(String email, String name, String link, String endTime);

    // Sent to candidate when they PASS the assessment — invites them to interview
    void sendInterviewInvitation(String email, String name, String assessmentTitle, double percentage);

    // Sent when admin sets the interview date
    void sendInterviewInvitationWithDate(String email, String name, String assessmentTitle, double percentage, java.time.LocalDateTime interviewDate);
}