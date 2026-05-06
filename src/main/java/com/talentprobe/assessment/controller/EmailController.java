package com.talentprobe.assessment.controller;

import com.talentprobe.assessment.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/invite")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<String> sendInvitation(@RequestBody Map<String, String> req) {
        emailService.sendInvitationEmail(
                req.get("candidateEmail"),
                req.get("candidateName"),
                req.get("invitationLink")
        );
        return ResponseEntity.ok("Invitation sent to " + req.get("candidateEmail"));
    }
}