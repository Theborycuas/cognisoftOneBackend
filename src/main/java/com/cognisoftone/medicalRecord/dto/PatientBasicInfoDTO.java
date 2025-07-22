package com.cognisoftone.medicalRecord.dto;

import lombok.Data;

@Data
public class PatientBasicInfoDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String identification;
    private String email;
    private String phone;
    private Boolean consent;
}

