package com.talentprobe.assessment.service;

public interface EmailService {
    void sendInvitationEmail(String toEmail, String candidateName, String invitationLink);
}