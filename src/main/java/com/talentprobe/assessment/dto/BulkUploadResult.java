package com.talentprobe.assessment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class BulkUploadResult {
    private int created;
    private int skipped;
    private List<String> errors;
}