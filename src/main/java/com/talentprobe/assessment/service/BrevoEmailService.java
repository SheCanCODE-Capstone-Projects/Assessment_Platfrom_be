package com.talentprobe.assessment.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BrevoEmailService implements EmailService {

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BREVO_API_URL = "https://api.brevo.com/v3/smtp/email";

    @Override
    public void sendInvitationEmail(String toEmail, String candidateName, String invitationLink) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", apiKey);

            String htmlContent = "<h3>Hello " + candidateName + ",</h3>"
                    + "<p>You have been invited to complete an assessment on TalentProbe.</p>"
                    + "<p><a href=\"" + invitationLink + "\" style=\"background:#4CAF50;color:white;padding:12px 24px;text-decoration:none;border-radius:5px;display:inline-block;\">Start Assessment</a></p>"
                    + "<p>Or copy this link: " + invitationLink + "</p>"
                    + "<p>Best regards,<br>TalentProbe Team</p>";

            Map<String, Object> sender = Map.of(
                    "name", senderName,
                    "email", senderEmail
            );

            Map<String, Object> to = Map.of(
                    "email", toEmail,
                    "name", candidateName
            );

            Map<String, Object> body = Map.of(
                    "sender", sender,
                    "to", List.of(to),
                    "subject", "TalentProbe Assessment Invitation",
                    "htmlContent", htmlContent
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(BREVO_API_URL, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Invitation email sent to {}", toEmail);
            } else {
                throw new RuntimeException("Brevo API returned: " + response.getStatusCode());
            }

        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Email sending failed", e);
        }
    }
}