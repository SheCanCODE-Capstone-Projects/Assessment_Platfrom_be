package com.talentprobe.assessment.config;

import com.talentprobe.assessment.service.CandidateAssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AssessmentSessionScheduler {

    @Autowired
    private CandidateAssessmentService candidateAssessmentService;

    // Runs every 30 seconds to check for expired attempts and auto-submit them
    @Scheduled(fixedRate = 30000)
    public void autoSubmitExpiredAttempts() {
        candidateAssessmentService.autoSubmitExpiredAttempts();
    }
}
