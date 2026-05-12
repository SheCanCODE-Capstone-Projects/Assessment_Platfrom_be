package com.talentprobe.assessment.controller;

import com.talentprobe.assessment.dto.AssignmentRequestDTO;
import com.talentprobe.assessment.dto.AssignmentResponseDTO;
import com.talentprobe.assessment.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {
    @Autowired
    private AssignmentService assignmentService;

    @PostMapping
    public ResponseEntity<AssignmentResponseDTO> assignCandidate(
            @RequestBody AssignmentRequestDTO request) {

        AssignmentResponseDTO response = assignmentService.assignCandidate(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<AssignmentResponseDTO> getAssignmentById(
            @PathVariable UUID id) {

        AssignmentResponseDTO response = assignmentService.getAssignmentById(id);
        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<List<AssignmentResponseDTO>> getAllAssignments() {

        List<AssignmentResponseDTO> response = assignmentService.getAllAssignments();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam String token) {

        boolean isValid = assignmentService.validateToken(token);

        if (isValid) {
            AssignmentResponseDTO response = assignmentService.getAssignmentByToken(token);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }
    }


    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<AssignmentResponseDTO>> getAssignmentsByCandidate(
            @PathVariable UUID candidateId) {

        List<AssignmentResponseDTO> response = assignmentService
                .getAssignmentsByCandidate(candidateId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/assessment/{assessmentId}")
    public ResponseEntity<List<AssignmentResponseDTO>> getAssignmentsByAssessment(
            @PathVariable UUID assessmentId) {

        List<AssignmentResponseDTO> response = assignmentService
                .getAssignmentsByAssessment(assessmentId);
        return ResponseEntity.ok(response);
    }

}
