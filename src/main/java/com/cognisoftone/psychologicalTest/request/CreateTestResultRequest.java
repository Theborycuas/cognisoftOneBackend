package com.cognisoftone.psychologicalTest.request;

import lombok.Data;

@Data
public class CreateTestResultRequest {
    private Long testId;
    private Long patientId;
    private String autoSummary;
    private String observations;
}
