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
public class BrevoEmailService implements EmailService {

    private final RestTemplate rest = new RestTemplate();

    @Value("${BREVO_API_KEY}")
    private String apiKey;

    @Value("${BREVO_SENDER_EMAIL}")
    private String senderEmail;

    @Value("${BREVO_SENDER_NAME}")
    private String senderName;

    private static final String BREVO_URL = "https://api.brevo.com/v3/smtp/email";

    // Backward compatible - calls new method with TBD times
    @Override
    public void sendInvitation(String email, String name, String link) {
        sendInvitation(email, name, link, "TBD", "TBD");
    }

    // New method with start and end time
    @Override
    public void sendInvitation(String email, String name, String link, String startTime, String endTime) {
        String html = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
            </head>
            <body style="font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif; line-height:1.6; color:#1a1a1a; margin:0; padding:0; background-color:#ffffff;">
                <div style="max-width:600px;margin:0 auto;padding:40px 24px;">
                    <h1 style="font-size:24px;font-weight:600;color:#0f172a;margin:0 0 24px 0;">Assessment Invitation</h1>
                    
                    <p style="font-size:16px;margin:0 0 16px 0;">Dear %s,</p>
                    
                    <p style="font-size:16px;margin:0 0 24px 0;">
                        You have been invited to complete an assessment. Please review the details below and complete the assessment within the specified timeframe.
                    </p>
                    
                    <div style="background:#f8fafc;border:1px solid #e2e8f0;border-radius:8px;padding:20px;margin:0 0 28px 0;">
                        <p style="margin:0 0 12px 0;font-weight:600;font-size:14px;text-transform:uppercase;letter-spacing:0.5px;color:#475569;">
                            Assessment Window
                        </p>
                        <p style="margin:0 0 8px 0;font-size:15px;"><strong>Start:</strong> %s</p>
                        <p style="margin:0;font-size:15px;"><strong>End:</strong> %s</p>
                    </div>
                    
                    <div style="text-align:center;margin:32px 0;">
                        <a href="%s" 
                           style="background:#2563eb;color:#ffffff;padding:14px 32px;text-decoration:none;border-radius:6px;display:inline-block;font-weight:600;font-size:16px;">
                            Start Assessment
                        </a>
                    </div>
                    
                    <div style="background:#fef2f2;border-left:4px solid #dc2626;padding:12px 16px;margin:28px 0;">
                        <p style="margin:0;font-size:14px;color:#991b1b;">
                            <strong>Note:</strong> This link will expire at %s. Please complete the assessment before this time.
                        </p>
                    </div>
                    
                    <p style="font-size:14px;color:#64748b;margin:0 0 8px 0;">
                        This link is unique to you and should not be shared.
                    </p>
                    
                    <p style="font-size:16px;margin:40px 0 0 0;">
                        Best regards,<br>
                        The TalentProbe Team
                    </p>
                    
                    <hr style="border:none;border-top:1px solid #e2e8f0;margin:40px 0 20px 0;">
                    <p style="font-size:12px;color:#94a3b8;margin:0;">
                        If you did not expect this invitation, please disregard this email.
                    </p>
                </div>
            </body>
            </html>
            """.formatted(name, startTime, endTime, link, endTime);

        send(email, name, "TalentProbe: Assessment Invitation", html);
    }


    @Override
    public void sendReminder(String email, String name, String link) {
        sendReminder(email, name, link, "TBD");
    }

    @Override
    public void sendReminder(String email, String name, String link, String endTime) {
        String html = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
            </head>
            <body style="font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif; line-height:1.6; color:#1a1a1a; margin:0; padding:0;">
                <div style="max-width:600px;margin:0 auto;padding:40px 24px;">
                    <h2 style="font-size:22px;font-weight:600;color:#0f172a;margin:0 0 20px 0;">Assessment Reminder</h2>
                    
                    <p style="font-size:16px;margin:0 0 16px 0;">Dear %s,</p>
                    
                    <p style="font-size:16px;margin:0 0 24px 0;">
                        This is a reminder to complete your assessment. Your assessment window is ending soon.
                    </p>
                    
                    <div style="background:#fefce8;border:1px solid #facc15;border-radius:8px;padding:16px;margin:0 0 28px 0;">
                        <p style="margin:0;font-size:15px;"><strong>Expires at:</strong> %s</p>
                    </div>
                    
                    <div style="text-align:center;margin:32px 0;">
                        <a href="%s" 
                           style="background:#2563eb;color:#ffffff;padding:14px 32px;text-decoration:none;border-radius:6px;display:inline-block;font-weight:600;font-size:16px;">
                            Continue Assessment
                        </a>
                    </div>
                    
                    <p style="font-size:16px;margin:40px 0 0 0;">
                        Best regards,<br>
                        The TalentProbe Team
                    </p>
                </div>
            </body>
            </html>
            """.formatted(name, endTime, link);

        send(email, name, "TalentProbe: Assessment Reminder", html);
    }

    @Override
    public void sendResult(String email, String name, String link) {
        String html = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
            </head>
            <body style="font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif; line-height:1.6; color:#1a1a1a; margin:0; padding:0;">
                <div style="max-width:600px;margin:0 auto;padding:40px 24px;">
                    <h2 style="font-size:22px;font-weight:600;color:#0f172a;margin:0 0 20px 0;">Assessment Results Available</h2>
                    
                    <p style="font-size:16px;margin:0 0 16px 0;">Dear %s,</p>
                    
                    <p style="font-size:16px;margin:0 0 28px 0;">
                        Your assessment has been completed and the results are now available for review.
                    </p>
                    
                    <div style="text-align:center;margin:32px 0;">
                        <a href="%s" 
                           style="background:#2563eb;color:#ffffff;padding:14px 32px;text-decoration:none;border-radius:6px;display:inline-block;font-weight:600;font-size:16px;">
                            View Results
                        </a>
                    </div>
                    
                    <p style="font-size:16px;margin:40px 0 0 0;">
                        Best regards,<br>
                        The TalentProbe Team
                    </p>
                </div>
            </body>
            </html>
            """.formatted(name, link);

        send(email, name, "TalentProbe: Assessment Results", html);
    }

    private void send(String toEmail, String name, String subject, String html) {
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
}