package com.cognisoftone.psychologicalTest.response;

import lombok.Data;

import java.util.List;

@Data
public class FillTestResponse {

    private String testTitle;
    private List<QuestionDTO> questions;

    public FillTestResponse(String testTitle, List<QuestionDTO> questions) {
        this.testTitle = testTitle;
        this.questions = questions;
    }

    @Data
    public static class QuestionDTO {
        private Long questionId;
        private String text;

        public QuestionDTO(Long questionId, String text) {
            this.questionId = questionId;
            this.text = text;
        }

    }
}

