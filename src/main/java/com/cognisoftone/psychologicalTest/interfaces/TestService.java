package com.cognisoftone.psychologicalTest.interfaces;

import com.cognisoftone.psychologicalTest.model.TestModel;
import com.cognisoftone.psychologicalTest.request.AssignTestRequest;
import com.cognisoftone.psychologicalTest.response.AssignTestResponse;
import com.cognisoftone.psychologicalTest.response.FillTestResponse;

import java.util.List;
import java.util.Optional;

public interface TestService {
    TestModel createTest(TestModel test);
    Optional<TestModel> getTestById(Long id);
    List<TestModel> getAllActiveTests();
    AssignTestResponse assignTest(AssignTestRequest request);
    FillTestResponse getTestByToken(String token);
}
