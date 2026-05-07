package com.talentprobe.assessment.email;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final BrevoEmailService emailService;

    @PostMapping("/invite")
    public ResponseEntity<EmailResponse> sendInvite(@Valid @RequestBody InvitationRequest req) {
        emailService.sendInvitation(req.getEmail(), req.getName(), req.getLink());
        return ResponseEntity.ok(new EmailResponse(true, "Invitation sent successfully"));
    }

    @PostMapping("/reminder")
    public ResponseEntity<EmailResponse> sendReminder(@Valid @RequestBody ReminderRequest req) {
        emailService.sendReminder(req.getEmail(), req.getName(), req.getLink());
        return ResponseEntity.ok(new EmailResponse(true, "Reminder sent successfully"));
    }

    @PostMapping("/result")
    public ResponseEntity<EmailResponse> sendResult(@Valid @RequestBody ResultRequest req) {
        emailService.sendResult(req.getEmail(), req.getName(), req.getLink());
        return ResponseEntity.ok(new EmailResponse(true, "Result sent successfully"));
    }
}