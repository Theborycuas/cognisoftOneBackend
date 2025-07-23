package com.cognisoftone.psychologicalTest.service;

import com.cognisoftone.psychologicalTest.interfaces.QuestionService;
import com.cognisoftone.psychologicalTest.model.QuestionModel;
import com.cognisoftone.psychologicalTest.repository.QuestionRepository;
import com.cognisoftone.psychologicalTest.response.QuestionResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    @Override
    public QuestionModel createQuestion(QuestionModel question) {
        return questionRepository.save(question);
    }

    @Override
    public List<QuestionResponseDTO> getQuestionsByTestId(Long testId) {
        List<QuestionModel> questions = questionRepository.findByTest_Id(testId);
        return questions.stream()
                .map(q -> new QuestionResponseDTO(
                        q.getId(),
                        q.getStatement(),
                        q.getType(),
                        q.getOptions(),
                        q.getCreatedAt(),
                        q.getUpdatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteQuestionsByTestId(Long testId) {
        questionRepository.deleteByTestId(testId);
    }
}

