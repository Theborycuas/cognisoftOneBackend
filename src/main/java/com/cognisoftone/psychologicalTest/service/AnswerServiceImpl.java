package com.cognisoftone.psychologicalTest.service;

import com.cognisoftone.psychologicalTest.interfaces.AnswerService;
import com.cognisoftone.psychologicalTest.model.AnswerModel;
import com.cognisoftone.psychologicalTest.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;

    @Override
    public List<AnswerModel> saveAnswers(List<AnswerModel> answers) {
        return answerRepository.saveAll(answers);
    }

    @Override
    public List<AnswerModel> getAnswersByTestId(Long testId) {
        return answerRepository.findByTestId(testId);
    }

    @Override
    public List<AnswerModel> getAnswersByUserAndTest(Long userId, Long testId) {
        return answerRepository.findByUserIdAndTestId(userId, testId);
    }
}

