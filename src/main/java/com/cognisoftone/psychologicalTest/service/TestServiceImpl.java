package com.cognisoftone.psychologicalTest.service;

import com.cognisoftone.psychologicalTest.interfaces.TestService;
import com.cognisoftone.psychologicalTest.model.TestAssignment;
import com.cognisoftone.psychologicalTest.model.TestModel;
import com.cognisoftone.psychologicalTest.repository.TestAssignmentRepository;
import com.cognisoftone.psychologicalTest.repository.TestRepository;
import com.cognisoftone.psychologicalTest.request.AssignTestRequest;
import com.cognisoftone.psychologicalTest.response.AssignTestResponse;
import com.cognisoftone.psychologicalTest.response.FillTestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;
    private final TestAssignmentRepository assignmentRepository;

    @Override
    public TestModel createTest(TestModel test) {
        test.setActive(true);
        return testRepository.save(test);
    }

    @Override
    public Optional<TestModel> getTestById(Long id) {
        return testRepository.findById(id);
    }

    @Override
    public List<TestModel> getAllActiveTests() {
        return testRepository.findByActiveTrue();
    }

    @Override
    public AssignTestResponse assignTest(AssignTestRequest request) {
        TestModel test = testRepository.findById(request.getTestId())
                .orElseThrow(() -> new RuntimeException("Test not found"));

        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusDays(request.getValidDays());

        TestAssignment assignment = new TestAssignment();
        assignment.setTest(test);
        assignment.setToken(token);
        assignment.setCreatedAt(now);
        assignment.setExpiresAt(expiresAt);
        assignment.setFilled(false);

        assignmentRepository.save(assignment);

        String link = "https://tusistema.com/api/test/fill/" + token;

        return new AssignTestResponse(link, expiresAt);
    }

    public FillTestResponse getTestByToken(String token) {
        TestAssignment assignment = assignmentRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (assignment.isFilled()) {
            throw new RuntimeException("Este test ya fue completado.");
        }

        if (assignment.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("El enlace ha expirado.");
        }

        TestModel test = assignment.getTest();

        List<FillTestResponse.QuestionDTO> questions = test.getQuestions().stream()
                .map(q -> new FillTestResponse.QuestionDTO(q.getId(), q.getStatement()))
                .collect(Collectors.toList());

        return new FillTestResponse(test.getName(), questions);
    }


}

