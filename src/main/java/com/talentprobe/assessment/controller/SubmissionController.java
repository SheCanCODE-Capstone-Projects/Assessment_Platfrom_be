package com.talentprobe.assessment.controller;

import com.talentprobe.assessment.dto.GradeSubmissionRequest;
import com.talentprobe.assessment.dto.SubmissionDto;
import com.talentprobe.assessment.dto.SubmissionRequest;
import com.talentprobe.assessment.service.SubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/submissions")
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;


    @PostMapping
    public ResponseEntity<SubmissionDto> submitCode(@Valid @RequestBody SubmissionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(submissionService.submitCode(request));
    }



    @GetMapping("/{submissionId}")
    public ResponseEntity<SubmissionDto> getSubmissionById(@PathVariable UUID submissionId) {
        return ResponseEntity.ok(submissionService.getSubmissionById(submissionId));
    }



    @GetMapping("/attempt/{candidateAssessmentId}")
    public ResponseEntity<List<SubmissionDto>> getSubmissionsByAttempt(
            @PathVariable UUID candidateAssessmentId) {
        return ResponseEntity.ok(submissionService.getSubmissionsByAttempt(candidateAssessmentId));
    }


    @PatchMapping("/{submissionId}/grade")
    public ResponseEntity<SubmissionDto> gradeSubmission(
            @PathVariable UUID submissionId,
            @Valid @RequestBody GradeSubmissionRequest request) {
        return ResponseEntity.ok(submissionService.saveSubmission(submissionId, request));
    }
}
