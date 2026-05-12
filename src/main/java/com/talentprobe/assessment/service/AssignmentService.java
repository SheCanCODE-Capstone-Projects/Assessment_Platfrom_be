package com.talentprobe.assessment.service;
import com.talentprobe.assessment.dto.AssignmentRequestDTO;
import com.talentprobe.assessment.dto.AssignmentResponseDTO;
import com.talentprobe.assessment.entity.Assessment;
import com.talentprobe.assessment.entity.CandidateAssignment;
import com.talentprobe.assessment.exception.ResourceNotFoundException;
import com.talentprobe.assessment.mapper.AssignmentMapper;
import com.talentprobe.assessment.repository.AssessmentRepository;
import com.talentprobe.assessment.repository.AssignmentRepository;
import com.talentprobe.assessment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.talentprobe.assessment.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AssignmentService {
    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private AssignmentMapper assignmentMapper;

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Value("${app.base-url}")
    private String baseUrl;

    public AssignmentResponseDTO assignCandidate(AssignmentRequestDTO request) {

        Assessment assessment = assessmentRepository
                .findById(request.getAssessmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found"));

        User candidate = userRepository
                .findByUserIdAndDeletedAtIsNull(request.getCandidateId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

        String token = UUID.randomUUID().toString();

        String accessLink = baseUrl + "/" + token;
        // Build assignment
        CandidateAssignment assignment = CandidateAssignment.builder()
                .assessment(assessment)
                .candidate(candidate)
                .secureToken(UUID.randomUUID().toString())
                .linkExpiry(LocalDateTime.now().plusHours(48))
                .assignedAt(LocalDateTime.now())
                .build();

        CandidateAssignment saved = assignmentRepository.save(assignment);

        return assignmentMapper.toResponseDTO(saved);
    }
    public boolean validateToken(String token) {
        return assignmentRepository.findBySecureToken(token)
                .map(a -> a.getLinkExpiry().isAfter(LocalDateTime.now()))
                .orElse(false);
    }

    //  GET ASSIGNMENT BY ID

    public AssignmentResponseDTO getAssignmentById(UUID assignmentId) {
        CandidateAssignment assignment = assignmentRepository
                .findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));

        return assignmentMapper.toResponseDTO(assignment);
    }
    //GET ALL ASSIGNMENTS

    public List<AssignmentResponseDTO> getAllAssignments() {
        return assignmentRepository.findAll()
                .stream()
                .map(assignmentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    // GET BY TOKEN (for candidate access)
    public AssignmentResponseDTO getAssignmentByToken(String token) {
        CandidateAssignment assignment = assignmentRepository
                .findBySecureToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid token"));

        return assignmentMapper.toResponseDTO(assignment);
    }
    public List<AssignmentResponseDTO> getPendingAssignments() {
        return assignmentRepository.findByLinkExpiryAfter(LocalDateTime.now())
                .stream()
                .map(assignmentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    public List<AssignmentResponseDTO> getAssignmentsByCandidate(UUID candidateId) {
        return assignmentRepository.findByCandidateUserId(candidateId)
                .stream()
                .map(assignmentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    public List<AssignmentResponseDTO> getAssignmentsByAssessment(UUID assessmentId) {
        return assignmentRepository.findByAssessmentAssessmentId(assessmentId)
                .stream()
                .map(assignmentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
