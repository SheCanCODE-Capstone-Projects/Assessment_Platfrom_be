package com.talentprobe.assessment.email;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BrevoEmailService {

    private final RestTemplate rest = new RestTemplate();

    @Value("${brevo.api-key}")
    private String apiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    private static final String BREVO_URL = "https://api.brevo.com/v3/smtp/email";

    public void sendInvitation(String email, String name, String link) {
        String html = buildInviteHtml(name, link);
        send(email, name, link, "TalentProbe: Assessment Invitation", html);
    }

    public void sendReminder(String email, String name, String link) {
        String html = buildReminderHtml(name, link);
        send(email, name, link, "TalentProbe: Assessment Reminder", html);
    }

    public void sendResult(String email, String name, String link) {
        String html = buildResultHtml(name, link);
        send(email, name, link, "TalentProbe: Assessment Results", html);
    }

    private void send(String toEmail, String name, String link, String subject, String html) {
        // Manual URL check instead of @Pattern to avoid HV000032
        if (link == null || (!link.startsWith("http://") && !link.startsWith("https://"))) {
            throw new EmailDeliveryException("Link must start with http:// or https://", HttpStatus.BAD_REQUEST);
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", apiKey);

            Map<String, Object> payload = Map.of(
                    "sender", Map.of("email", senderEmail, "name", senderName),
                    "to", List.of(Map.of("email", toEmail, "name", name)),
                    "subject", subject,
                    "htmlContent", html
            );

            ResponseEntity<String> resp = rest.postForEntity(BREVO_URL, new HttpEntity<>(payload, headers), String.class);
            System.out.println("Brevo response: " + resp.getStatusCode());

        } catch (Exception e) {
            throw new EmailDeliveryException("Brevo API failed: " + e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }

    private String buildInviteHtml(String name, String link) {
        return """
            <html>
            <body style="font-family: Arial, sans-serif; line-height:1.6;">
                <h2>Hi %s,</h2>
                <p>You have been invited to complete an assessment on TalentProbe.</p>
                <p>Click below to start:</p>
                <p><a href="%s" style="background:#4CAF50;color:white;padding:12px 20px;text-decoration:none;border-radius:5px;">Start Assessment</a></p>
                <p>This link is unique to you. Do not share it.</p>
                <p>Best,<br>TalentProbe Team</p>
            </body>
            </html>
            """.formatted(name, link);
    }

    private String buildReminderHtml(String name, String link) {
        return """
            <html>
            <body style="font-family: Arial, sans-serif; line-height:1.6;">
                <h2>Hi %s,</h2>
                <p>This is a reminder to complete your assessment.</p>
                <p><a href="%s" style="background:#2196F3;color:white;padding:12px 20px;text-decoration:none;border-radius:5px;">Continue Assessment</a></p>
                <p>Best,<br>TalentProbe Team</p>
            </body>
            </html>
            """.formatted(name, link);
    }

    private String buildResultHtml(String name, String link) {
        return """
            <html>
            <body style="font-family: Arial, sans-serif; line-height:1.6;">
                <h2>Hi %s,</h2>
                <p>Your assessment results are ready.</p>
                <p><a href="%s" style="background:#FF9800;color:white;padding:12px 20px;text-decoration:none;border-radius:5px;">View Results</a></p>
                <p>Best,<br>TalentProbe Team</p>
            </body>
            </html>
            """.formatted(name, link);
    }
}