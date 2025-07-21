package com.cognisoftone.psychologicalTest.service;

import com.cognisoftone.psychologicalTest.interfaces.TestResultService;
import com.cognisoftone.psychologicalTest.model.TestResult;
import com.cognisoftone.psychologicalTest.repository.TestResultRepository;
import com.cognisoftone.psychologicalTest.request.CreateTestResultRequest;
import com.cognisoftone.psychologicalTest.response.TestResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestResultServiceImpl implements TestResultService {

    private final TestResultRepository testResultRepository;

    @Override
    public TestResultResponse create(CreateTestResultRequest request) {
        TestResult result = new TestResult();
        result.setTestId(request.getTestId());
        result.setPatientId(request.getPatientId());
        result.setAutoSummary(request.getAutoSummary());
        result.setObservations(request.getObservations());

        TestResult saved = testResultRepository.save(result);
        return mapToResponse(saved);
    }

    @Override
    public List<TestResultResponse> getByPatientId(Long patientId) {
        return testResultRepository.findByPatientId(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TestResultResponse> getByTestId(Long testId) {
        return testResultRepository.findByTestId(testId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private TestResultResponse mapToResponse(TestResult result) {
        TestResultResponse dto = new TestResultResponse();
        dto.setId(result.getId());
        dto.setTestId(result.getTestId());
        dto.setPatientId(result.getPatientId());
        dto.setAutoSummary(result.getAutoSummary());
        dto.setObservations(result.getObservations());
        dto.setCompletedAt(result.getCompletedAt());
        dto.setCreatedAt(result.getCreatedAt());
        dto.setUpdatedAt(result.getUpdatedAt());
        return dto;
    }
}

