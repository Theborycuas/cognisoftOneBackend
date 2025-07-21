package com.cognisoftone.psychologicalTest.repository;

import com.cognisoftone.psychologicalTest.model.AnswerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<AnswerModel, Long> {

    List<AnswerModel> findByUserIdAndTestId(Long userId, Long testId);

    List<AnswerModel> findByTestId(Long testId);

    @Query("SELECT a FROM AnswerModel a WHERE a.userId IS NULL AND a.testId = :testId")
    List<AnswerModel> findAnonymousByTestId(@Param("testId") Long testId);
}
