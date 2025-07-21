package com.cognisoftone.psychologicalTest.service;

import com.cognisoftone.psychologicalTest.interfaces.TestService;
import com.cognisoftone.psychologicalTest.model.TestModel;
import com.cognisoftone.psychologicalTest.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;

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
}

