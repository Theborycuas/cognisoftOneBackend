package com.cognisoftone.psychologicalTest.response;

import com.cognisoftone.psychologicalTest.dto.TestAnswerDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TestResultResponse {
    private Long id;
    private Long testId;
    private String testName;
    private Long patientId;
    private String autoSummary;
    private String observations;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TestAnswerDTO> answers;

}
