package com.cognisoftone.psychologicalTest.service;

import com.cognisoftone.medicalRecord.model.MedicalRecord;
import com.cognisoftone.medicalRecord.repository.MedicalRecordRepository;
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
    private final TestResultRepository testResultRepository;
    private final MedicalRecordRepository medicalRecordRepository;

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
            autoRecord.setPsychologistId(null); // aún no lo hay
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



}

