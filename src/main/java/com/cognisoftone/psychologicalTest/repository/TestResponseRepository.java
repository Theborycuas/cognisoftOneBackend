package com.cognisoftone.psychologicalTest.repository;

import com.cognisoftone.psychologicalTest.model.answers.TestResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestResponseRepository extends JpaRepository<TestResponse, Long> {
}
