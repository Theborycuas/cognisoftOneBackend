package com.cognisoftone.psychologicalTest.repository;

import com.cognisoftone.psychologicalTest.model.TestAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestAssignmentRepository extends JpaRepository<TestAssignment, Long> {
    Optional<TestAssignment> findByToken(String token);
}
