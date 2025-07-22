package com.cognisoftone.psychologicalTest.repository;

import com.cognisoftone.psychologicalTest.model.answers.TestResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TestResponseRepository extends JpaRepository<TestResponse, Long> {

    List<TestResponse> findByUser_Id(Long userId);
    Optional<TestResponse> findByTest_IdAndUser_Id(Long testId, Long userId);
}
