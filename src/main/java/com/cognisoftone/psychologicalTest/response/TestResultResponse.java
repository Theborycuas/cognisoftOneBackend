package com.cognisoftone.psychologicalTest.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TestResultResponse {
    private Long id;
    private Long testId;
    private Long patientId;
    private String autoSummary;
    private String observations;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
