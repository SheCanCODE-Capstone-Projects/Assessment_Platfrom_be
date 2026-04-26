package com.talentprobe.assessment.dto;

import lombok.Data;

@Data
public class UserPatchRequest {
    private String name;
    private String phoneNumber;
}