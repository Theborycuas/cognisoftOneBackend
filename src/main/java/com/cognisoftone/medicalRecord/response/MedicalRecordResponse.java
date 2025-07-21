package com.cognisoftone.medicalRecord.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MedicalRecordResponse {
    private Long id;
    private Long patientId;
    private Long psychologistId;
    private Long appointmentId;
    private Long testId;

    private String context;
    private String findings;
    private String diagnosis;
    private String recommendations;
    private String psychologistNotes;
    private Boolean reviewed;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

