package com.cognisoftone.psychologicalTest.repository;

import com.cognisoftone.psychologicalTest.model.TestModel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestRepository extends JpaRepository<TestModel, Long> {

    List<TestModel> findByActiveTrue();

    @EntityGraph(attributePaths = {"questions", "questions.options"})
    Optional<TestModel> findWithQuestionsById(Long id);

}
