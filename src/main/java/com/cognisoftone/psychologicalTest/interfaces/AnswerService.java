package com.cognisoftone.psychologicalTest.interfaces;

import com.cognisoftone.psychologicalTest.model.AnswerModel;

import java.util.List;

public interface AnswerService {
    List<AnswerModel> saveAnswers(List<AnswerModel> answers);
    List<AnswerModel> getAnswersByTestId(Long testId);
    List<AnswerModel> getAnswersByUserAndTest(Long userId, Long testId);
}