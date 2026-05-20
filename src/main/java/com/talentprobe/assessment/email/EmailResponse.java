package com.talentprobe.assessment.email;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailResponse {
    private boolean success;
    private String message;
}