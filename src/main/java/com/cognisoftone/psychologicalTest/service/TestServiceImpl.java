package com.cognisoftone.psychologicalTest.service;

import com.cognisoftone.common.exception.InvalidTokenException;
import com.cognisoftone.common.exception.UnprocessableEntityException;
import com.cognisoftone.medicalRecord.model.MedicalRecord;
import com.cognisoftone.medicalRecord.repository.MedicalRecordRepository;
import com.cognisoftone.psychologicalTest.dto.*;
import com.cognisoftone.psychologicalTest.interfaces.TestService;
import com.cognisoftone.psychologicalTest.model.QuestionModel;
import com.cognisoftone.psychologicalTest.model.TestAssignment;
import com.cognisoftone.psychologicalTest.model.TestModel;
import com.cognisoftone.psychologicalTest.model.TestResult;
import com.cognisoftone.psychologicalTest.model.answers.TestAnswer;
import com.cognisoftone.psychologicalTest.model.answers.TestResponse;
import com.cognisoftone.psychologicalTest.repository.*;
import com.cognisoftone.psychologicalTest.request.AssignTestRequest;
import com.cognisoftone.psychologicalTest.request.SubmitTestRequest;
import com.cognisoftone.psychologicalTest.response.AssignTestResponse;
import com.cognisoftone.psychologicalTest.response.FillTestResponse;
import com.cognisoftone.users.model.UserModel;
import com.cognisoftone.users.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

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
    private final TestResultRepository testResultRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    @Value("${frontend.base-url}")
    private String frontendBaseUrl;

    @Override
    public TestModel createTest(TestModel test) {
        test.setActive(true);

        // Guarda solo el test para obtener el ID
        TestModel savedTest = testRepository.save(test);

        // Setea el test en cada pregunta y guarda todas las preguntas
        if (test.getQuestions() != null) {
            for (QuestionModel question : test.getQuestions()) {
                question.setTest(savedTest);
            }
        }

        // Vuelve a guardar el test con las preguntas actualizadas
        return testRepository.save(savedTest);
    }

    @Override
    public TestDetailDTO getTestWithQuestions(Long testId) {
        TestModel test = testRepository.findById(testId)
                .orElseThrow(() -> new EntityNotFoundException("Test not found"));

        List<QuestionDTO> questionDTOs = test.getQuestions().stream().map(q ->
                new QuestionDTO(
                        q.getId(),
                        q.getStatement(),
                        q.getType(),
                        q.getOptions()
                )
        ).toList();

        return new TestDetailDTO(
                test.getId(),
                test.getName(),
                test.getDescription(),
                questionDTOs
        );
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
    public List<TestSummaryDTO> getAllTestSummaries() {
        return testRepository.findAll().stream().map(test -> {
            int totalQuestions = test.getQuestions().size();

            int estimatedTime = 0;
            if (totalQuestions <= 9) estimatedTime = 5;
            else if (totalQuestions <= 21) estimatedTime = 10;
            else if (totalQuestions <= 30) estimatedTime = 15;
            else estimatedTime = 20;

            return new TestSummaryDTO(
                    test.getId(),
                    test.getName(),
                    test.getDescription(),
                    test.getCategory() != null ? test.getCategory().getName() : "Sin categoría",
                    test.isActive(),
                    totalQuestions,
                    estimatedTime,
                    test.getCreatedAt().toLocalDate()
            );
        }).toList();
    }

    @Override
    public AssignTestResponse assignTest(AssignTestRequest request) {
        TestModel test = testRepository.findById(request.getTestId())
                .orElseThrow(() -> new EntityNotFoundException("Test with id " + request.getTestId() + " not found"));

        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusDays(request.getValidDays());

        TestAssignment assignment = new TestAssignment();
        assignment.setTest(test);
        assignment.setToken(token);
        assignment.setCreatedAt(now);
        assignment.setExpiresAt(expiresAt);
        assignment.setFilled(false);
        assignment.setPsychologistId(request.getPsychologistId());

        assignmentRepository.save(assignment);

        String link = frontendBaseUrl + "/psycologicalTests/link/" + token;

        return new AssignTestResponse(link, expiresAt);
    }

    public FillTestResponse getTestByToken(String token) {
        TestAssignment assignment = assignmentRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Token inválido"));

        if (assignment.isFilled()) {
            throw new UnprocessableEntityException("This test has already been completed.");
        }

        if (assignment.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new UnprocessableEntityException("The test link has expired.");
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
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));

        if (assignment.isFilled()) {
            throw new UnprocessableEntityException("This test has already been completed.");
        }

        if (assignment.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new UnprocessableEntityException("The test link has expired.");
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
                    .orElseThrow(() -> new EntityNotFoundException("Question not found: " + dto.getQuestionId()));

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

        //Generate Test Result
        boolean alreadyExists = testResultRepository
                .findByTestIdAndPatientId(assignment.getTest().getId(), user.getId())
                .isPresent();

        if (!alreadyExists) {
            TestResult result = new TestResult();
            result.setTestId(assignment.getTest().getId());
            result.setPatientId(user.getId());

            String resumen = generarResumenAuto(testResponse); // opcional
            result.setAutoSummary(resumen);
            result.setObservations(""); // editable luego

            TestResult savedResult = testResultRepository.save(result);

            //Crear historia clínica automática vinculada al test
            MedicalRecord autoRecord = new MedicalRecord();
            autoRecord.setPatientId(user.getId());
            autoRecord.setPsychologistId(assignment.getPsychologistId());
            autoRecord.setTestId(savedResult.getTestId());
            autoRecord.setAppointmentId(null); // test externo
            autoRecord.setContext("Test respondido vía link externo.");
            autoRecord.setFindings("Auto-generado. Requiere revisión profesional.");
            autoRecord.setDiagnosis(null);
            autoRecord.setRecommendations(null);
            autoRecord.setPsychologistNotes(null);
            autoRecord.setReviewed(false);
            autoRecord.setCreatedAt(LocalDateTime.now());

            medicalRecordRepository.save(autoRecord);
        }
    }

    private String generarResumenAuto(TestResponse response) {
        return "Test completado con " + response.getAnswers().size() + " respuestas el " + response.getSubmittedAt();
    }

    @Override
    public List<CompletedTestSummaryDTO> getCompletedTestsByUser(Long userId) {
        return testResponseRepository.findByUser_Id(userId).stream()
                .map(response -> {
                    CompletedTestSummaryDTO dto = new CompletedTestSummaryDTO();
                    dto.setTestId(response.getTest().getId());
                    dto.setTestName(response.getTest().getName());
                    dto.setSubmittedAt(response.getSubmittedAt());
                    dto.setTestResultId(getResultId(response.getTest().getId(), userId)); // si deseas
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public CompletedTestDetailDTO getCompletedTestDetail(Long userId, Long testId) {
        TestResponse response = testResponseRepository.findByTest_IdAndUser_Id(testId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Test no encontrado"));

        List<TestAnswerDTO> answers = response.getAnswers().stream().map(answer -> {
            TestAnswerDTO dto = new TestAnswerDTO();
            dto.setQuestionId(answer.getQuestion().getId());
            dto.setQuestion(answer.getQuestion().getStatement());
            dto.setAnswer(answer.getAnswer());
            return dto;
        }).collect(Collectors.toList());

        CompletedTestDetailDTO detail = new CompletedTestDetailDTO();
        detail.setTestId(testId);
        detail.setTestName(response.getTest().getName());
        detail.setSubmittedAt(response.getSubmittedAt());
        detail.setAnswers(answers);
        return detail;
    }

    private Long getResultId(Long testId, Long userId) {
        return testResultRepository.findByTestIdAndPatientId(testId, userId)
                .map(TestResult::getId)
                .orElse(null);
    }

}

