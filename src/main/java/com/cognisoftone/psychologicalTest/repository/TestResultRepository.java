package com.cognisoftone.psychologicalTest.repository;

import com.cognisoftone.psychologicalTest.model.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestResultRepository extends JpaRepository<TestResult, Long> {

    List<TestResult> findByPatientId(Long patientId);

    List<TestResult> findByTestId(Long testId);

    Optional<TestResult> findByTestIdAndPatientId(Long testId, Long patientId);
}
