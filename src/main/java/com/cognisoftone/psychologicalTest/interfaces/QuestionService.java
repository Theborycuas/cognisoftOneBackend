package com.cognisoftone.psychologicalTest.interfaces;

import com.cognisoftone.psychologicalTest.model.QuestionModel;
import com.cognisoftone.psychologicalTest.response.QuestionResponseDTO;

import java.util.List;

public interface QuestionService {
    QuestionModel createQuestion(QuestionModel question);
    public List<QuestionResponseDTO> getQuestionsByTestId(Long testId);
    void deleteQuestionsByTestId(Long testId);
}
