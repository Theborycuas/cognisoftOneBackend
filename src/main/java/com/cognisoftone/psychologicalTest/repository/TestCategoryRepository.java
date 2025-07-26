package com.cognisoftone.psychologicalTest.repository;

import com.cognisoftone.psychologicalTest.model.TestCategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestCategoryRepository extends JpaRepository<TestCategoryModel, Long> {
    Optional<TestCategoryModel> findByName(String name);
}
