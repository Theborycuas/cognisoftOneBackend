package com.cognisoftone.psychologicalTest.request;

import lombok.Data;

import java.util.List;

@Data
public class SubmitTestRequest {

    private String firstName;
    private String identification;

    private List<AnswerDTO> answers;

    @Data
    public static class AnswerDTO {
        private Long questionId;
        private String answer;
    }
}
