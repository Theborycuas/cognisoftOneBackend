package com.cognisoftone.psychologicalTest.interfaces;

import com.cognisoftone.psychologicalTest.model.TestModel;

import java.util.List;
import java.util.Optional;

public interface TestService {
    TestModel createTest(TestModel test);
    Optional<TestModel> getTestById(Long id);
    List<TestModel> getAllActiveTests();
}
