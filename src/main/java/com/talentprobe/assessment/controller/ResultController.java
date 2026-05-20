package com.talentprobe.assessment.controller;

import com.talentprobe.assessment.dto.ApiResponse;
import com.talentprobe.assessment.dto.ResultResponseDTO;
import com.talentprobe.assessment.service.ScoringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/results")
@RequiredArgsConstructor
@Tag(name = "Scoring & Results")
public class ResultController {

    private final ScoringService scoringService;

    /**
     * GET /results/{attemptId}
     * Admin calls this after grading all submissions.
     * Calculates total score, percentage, PASS/FAIL.
     * If PASS → automatically sends interview invitation email.
     */
    @GetMapping("/{attemptId}")
    @Operation(
        summary = "Get result for an attempt",
        description = "Calculates total score from admin-graded submissions. " +
                      "Returns percentage, PASS/FAIL, per-question breakdown. " +
                      "Sends interview invitation email automatically if candidate PASSES. ADMIN only."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ResultResponseDTO>> getResult(@PathVariable UUID attemptId) {
        ResultResponseDTO result = scoringService.getResult(attemptId);
        return ResponseEntity.ok(ApiResponse.success("Result calculated successfully", result));
    }

    /**
     * PATCH /results/{attemptId}/interview-date
     * Admin sets the interview date for a qualified candidate.
     * Sends a follow-up email with the interview date.
     */
    @PatchMapping("/{attemptId}/interview-date")
    @Operation(
        summary = "Set interview date for qualified candidate",
        description = "Admin sets the interview date after candidate qualifies. " +
                      "Sends email to candidate with the date. ADMIN only."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ResultResponseDTO>> setInterviewDate(
            @PathVariable UUID attemptId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime interviewDate) {

        ResultResponseDTO result = scoringService.setInterviewDate(attemptId, interviewDate);
        return ResponseEntity.ok(ApiResponse.success("Interview date set and email sent", result));
    }
}
