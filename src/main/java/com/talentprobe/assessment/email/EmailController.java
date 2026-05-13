package com.talentprobe.assessment.email;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
@Tag(name = "2. Email", description = "Send assessment emails")
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/invite")
    @Operation(summary = "Send assessment invitation with start and end time")
    public ResponseEntity<String> sendInvitation(@Valid @RequestBody InvitationRequest request) {
        emailService.sendInvitation(
                request.getEmail(),
                request.getName(),
                request.getLink(),
                request.getStartTime(),
                request.getEndTime()
        );
        return ResponseEntity.ok("Invitation sent to " + request.getEmail());
    }

    @PostMapping("/reminder")
    @Operation(summary = "Send assessment reminder with end time")
    public ResponseEntity<String> sendReminder(@Valid @RequestBody ReminderRequest request) {
        emailService.sendReminder(
                request.getEmail(),
                request.getName(),
                request.getLink(),
                request.getEndTime()
        );
        return ResponseEntity.ok("Reminder sent to " + request.getEmail());
    }

    @PostMapping("/result")
    @Operation(summary = "Send assessment result")
    public ResponseEntity<String> sendResult(@Valid @RequestBody ResultRequest request) {
        emailService.sendResult(
                request.getEmail(),
                request.getName(),
                request.getLink()
        );
        return ResponseEntity.ok("Result sent to " + request.getEmail());
    }
}