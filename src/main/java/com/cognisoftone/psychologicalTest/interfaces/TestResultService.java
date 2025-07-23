package com.cognisoftone.psychologicalTest.interfaces;

import com.cognisoftone.psychologicalTest.request.CreateTestResultRequest;
import com.cognisoftone.psychologicalTest.response.TestResultResponse;

import java.util.List;

public interface TestResultService {

    TestResultResponse create(CreateTestResultRequest request);

    List<TestResultResponse> getByPatientId(Long patientId);

    List<TestResultResponse> getByTestId(Long testId);
}
