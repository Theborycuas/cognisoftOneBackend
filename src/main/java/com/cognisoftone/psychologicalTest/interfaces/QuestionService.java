package com.cognisoftone.psychologicalTest.interfaces;

import com.cognisoftone.psychologicalTest.model.QuestionModel;

import java.util.List;

public interface QuestionService {
    QuestionModel createQuestion(QuestionModel question);
    List<QuestionModel> getQuestionsByTestId(Long testId);
    void deleteQuestionsByTestId(Long testId);
}
