package com.cognisoftone.psychologicalTest.dto;

import lombok.Data;

@Data
public class TestAnswerDTO {
    private Long questionId;
    private String question;
    private String answer;
}
