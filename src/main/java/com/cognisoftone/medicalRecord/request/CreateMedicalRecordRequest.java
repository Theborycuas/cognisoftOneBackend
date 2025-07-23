package com.cognisoftone.medicalRecord.request;

import lombok.Data;

@Data
public class CreateMedicalRecordRequest {
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
}

