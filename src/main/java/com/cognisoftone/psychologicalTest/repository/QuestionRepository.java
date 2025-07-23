package com.cognisoftone.psychologicalTest.repository;

import com.cognisoftone.psychologicalTest.model.QuestionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionModel, Long> {

    List<QuestionModel> findByTest_Id(Long testId);

    void deleteByTestId(Long testId);
}
