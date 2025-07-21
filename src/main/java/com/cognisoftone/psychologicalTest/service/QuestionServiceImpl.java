package com.cognisoftone.psychologicalTest.service;

import com.cognisoftone.psychologicalTest.interfaces.QuestionService;
import com.cognisoftone.psychologicalTest.model.QuestionModel;
import com.cognisoftone.psychologicalTest.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    @Override
    public QuestionModel createQuestion(QuestionModel question) {
        return questionRepository.save(question);
    }

    @Override
    public List<QuestionModel> getQuestionsByTestId(Long testId) {
        return questionRepository.findByTestId(testId);
    }

    @Override
    public void deleteQuestionsByTestId(Long testId) {
        questionRepository.deleteByTestId(testId);
    }
}

