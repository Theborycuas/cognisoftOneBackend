package com.cognisoftone.psychologicalTest.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompletedTestSummaryDTO {
    private Long testId;
    private String testName;
    private LocalDateTime submittedAt;
    private Long testResultId;
}
