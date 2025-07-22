package com.cognisoftone.psychologicalTest.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CompletedTestDetailDTO {
    private Long testId;
    private String testName;
    private LocalDateTime submittedAt;
    private List<TestAnswerDTO> answers;
}

