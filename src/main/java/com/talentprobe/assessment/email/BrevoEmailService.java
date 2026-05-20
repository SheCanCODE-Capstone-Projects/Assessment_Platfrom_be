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
                            Start Assessment
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

    @Override
    public void sendInterviewInvitationWithDate(String email, String name, String assessmentTitle, double percentage, java.time.LocalDateTime interviewDate) {
        String formattedDate = interviewDate.format(java.time.format.DateTimeFormatter.ofPattern("dd MMMM yyyy 'at' HH:mm"));
        String html = """
            <!DOCTYPE html>
            <html>
            <head><meta charset="UTF-8"></head>
            <body style="font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; line-height:1.6; color:#1a1a1a; margin:0; padding:0;">
                <div style="max-width:600px;margin:0 auto;padding:40px 24px;">
                    <h1 style="font-size:24px;font-weight:600;color:#0f172a;margin:0 0 24px 0;">🎉 Congratulations — You Qualified!</h1>
                    <p style="font-size:16px;margin:0 0 16px 0;">Dear %s,</p>
                    <p style="font-size:16px;margin:0 0 24px 0;">
                        You have successfully passed the <strong>%s</strong> assessment
                        with a score of <strong>%.1f%%</strong>.
                    </p>
                    <div style="background:#f0fdf4;border:1px solid #86efac;border-radius:8px;padding:20px;margin:0 0 28px 0;">
                        <p style="margin:0 0 8px 0;font-size:15px;color:#166534;">✅ <strong>Qualification Status:</strong> QUALIFIED FOR INTERVIEW</p>
                        <p style="margin:0 0 8px 0;font-size:15px;color:#166534;">📊 <strong>Your Score:</strong> %.1f%%</p>
                        <p style="margin:0;font-size:15px;color:#166534;">📅 <strong>Interview Date:</strong> %s</p>
                    </div>
                    <p style="font-size:16px;margin:0 0 24px 0;">
                        Please prepare for your interview and make sure you are available on the selected date.
                        Our team will contact you with further details.
                    </p>
                    <p style="font-size:16px;margin:40px 0 0 0;">
                        Best regards,<br>The TalentProbe Team
                    </p>
                    <hr style="border:none;border-top:1px solid #e2e8f0;margin:40px 0 20px 0;">
                    <p style="font-size:12px;color:#94a3b8;margin:0;">If you have questions, please contact our recruitment team.</p>
                </div>
            </body>
            </html>
            """.formatted(name, assessmentTitle, percentage, percentage, formattedDate);

        send(email, name, "TalentProbe: Interview Invitation — " + formattedDate, html);
    }

    @Override
    public void sendInterviewInvitation(String email, String name, String assessmentTitle, double percentage) {
        String html = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
            </head>
            <body style="font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif; line-height:1.6; color:#1a1a1a; margin:0; padding:0; background-color:#ffffff;">
                <div style="max-width:600px;margin:0 auto;padding:40px 24px;">
                    <h1 style="font-size:24px;font-weight:600;color:#0f172a;margin:0 0 24px 0;">🎉 Congratulations — You Passed!</h1>

                    <p style="font-size:16px;margin:0 0 16px 0;">Dear %s,</p>

                    <p style="font-size:16px;margin:0 0 24px 0;">
                        We are pleased to inform you that you have successfully passed the
                        <strong>%s</strong> assessment with a score of <strong>%.1f%%</strong>.
                    </p>

                    <div style="background:#f0fdf4;border:1px solid #86efac;border-radius:8px;padding:20px;margin:0 0 28px 0;">
                        <p style="margin:0;font-size:15px;color:#166534;">
                            ✅ Based on your performance, we would like to invite you to the next stage of our selection process — an <strong>interview</strong>.
                        </p>
                    </div>

                    <p style="font-size:16px;margin:0 0 24px 0;">
                        Our team will be in touch shortly with the interview schedule and further details.
                        Please ensure your contact information is up to date.
                    </p>

                    <p style="font-size:16px;margin:40px 0 0 0;">
                        Best regards,<br>
                        The TalentProbe Team
                    </p>

                    <hr style="border:none;border-top:1px solid #e2e8f0;margin:40px 0 20px 0;">
                    <p style="font-size:12px;color:#94a3b8;margin:0;">
                        If you have any questions, please contact our recruitment team.
                    </p>
                </div>
            </body>
            </html>
            """.formatted(name, assessmentTitle, percentage);

        send(email, name, "TalentProbe: Interview Invitation — You Passed!", html);
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