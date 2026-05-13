package com.talentprobe.assessment.controller;

import com.talentprobe.assessment.dto.AttemptResponseDTO;
import com.talentprobe.assessment.dto.AttemptStartRequest;
import com.talentprobe.assessment.service.CandidateAssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/attempt")
public class CandidateAssessmentController {

    @Autowired
    private CandidateAssessmentService candidateAssessmentService;

    // POST /api/attempt/start
    @PostMapping("/start")
    public ResponseEntity<AttemptResponseDTO> startAssessment(@RequestBody AttemptStartRequest request) {
        AttemptResponseDTO response = candidateAssessmentService.startAssessment(request.getToken());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // PUT /api/attempt/{id}/submit
    @PutMapping("/{id}/submit")
    public ResponseEntity<AttemptResponseDTO> submitAssessment(@PathVariable UUID id) {
        AttemptResponseDTO response = candidateAssessmentService.submitAssessment(id);
        return ResponseEntity.ok(response);
    }

    // GET /api/attempt/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AttemptResponseDTO> getAttemptById(@PathVariable UUID id) {
        AttemptResponseDTO response = candidateAssessmentService.getAttemptById(id);
        return ResponseEntity.ok(response);
    }

    // GET /api/attempt/candidate/{candidateId}
    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<AttemptResponseDTO>> getAttemptsByCandidate(@PathVariable UUID candidateId) {
        List<AttemptResponseDTO> response = candidateAssessmentService.getAttemptsByCandidate(candidateId);
        return ResponseEntity.ok(response);
    }
}
