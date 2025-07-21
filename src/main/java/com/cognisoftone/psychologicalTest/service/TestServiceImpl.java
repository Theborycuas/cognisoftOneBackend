package com.cognisoftone.psychologicalTest.service;

import com.cognisoftone.psychologicalTest.interfaces.TestService;
import com.cognisoftone.psychologicalTest.model.QuestionModel;
import com.cognisoftone.psychologicalTest.model.TestAssignment;
import com.cognisoftone.psychologicalTest.model.TestModel;
import com.cognisoftone.psychologicalTest.model.answers.TestAnswer;
import com.cognisoftone.psychologicalTest.model.answers.TestResponse;
import com.cognisoftone.psychologicalTest.repository.QuestionRepository;
import com.cognisoftone.psychologicalTest.repository.TestAssignmentRepository;
import com.cognisoftone.psychologicalTest.repository.TestRepository;
import com.cognisoftone.psychologicalTest.repository.TestResponseRepository;
import com.cognisoftone.psychologicalTest.request.AssignTestRequest;
import com.cognisoftone.psychologicalTest.request.SubmitTestRequest;
import com.cognisoftone.psychologicalTest.response.AssignTestResponse;
import com.cognisoftone.psychologicalTest.response.FillTestResponse;
import com.cognisoftone.users.model.UserModel;
import com.cognisoftone.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;
    private final TestAssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final TestResponseRepository testResponseRepository;

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

    @Transactional
    public void submitTestResponse(String token, SubmitTestRequest request) {
        TestAssignment assignment = assignmentRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (assignment.isFilled()) {
            throw new RuntimeException("This test has already been completed.");
        }

        if (assignment.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("This test link has expired.");
        }

        // Find or create user
        UserModel user = userRepository.findByIdentification(request.getIdentification())
                .orElseGet(() -> {
                    UserModel newUser = new UserModel();
                    newUser.setFirstName(request.getFirstName());
                    newUser.setIdentification(request.getIdentification());
                    newUser.setConsent(true);
                    newUser.setEmail(request.getIdentification() + "@gmail.com");
                    newUser.setPassword(null);
                    return userRepository.save(newUser);
                });

        // Create test response
        TestResponse testResponse = new TestResponse();
        testResponse.setSubmittedAt(LocalDateTime.now());
        testResponse.setUser(user);
        testResponse.setTest(assignment.getTest());

        List<TestAnswer> answers = new ArrayList<>();

        for (SubmitTestRequest.AnswerDTO dto : request.getAnswers()) {
            QuestionModel question = questionRepository.findById(dto.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found: " + dto.getQuestionId()));

            TestAnswer answer = new TestAnswer();
            answer.setQuestion(question);
            answer.setAnswer(dto.getAnswer());
            answer.setResponse(testResponse);

            answers.add(answer);
        }

        testResponse.setAnswers(answers);
        testResponseRepository.save(testResponse);

        // Mark assignment as completed
        assignment.setFilled(true);
        assignmentRepository.save(assignment);
    }



}

