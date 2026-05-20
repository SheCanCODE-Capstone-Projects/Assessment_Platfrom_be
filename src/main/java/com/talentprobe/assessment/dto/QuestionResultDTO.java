package com.talentprobe.assessment.dto;

import com.talentprobe.assessment.enums.MarkingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResultDTO {

    private UUID questionId;
    private String questionTitle;
    private Integer maxMarks;
    private Integer earnedMarks;
    private String adminReviewNote;
    private MarkingStatus markingStatus;  // PENDING, PASSED, FAILED
}
