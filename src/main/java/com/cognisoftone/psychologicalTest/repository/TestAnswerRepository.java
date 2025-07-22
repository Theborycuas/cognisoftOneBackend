package com.cognisoftone.psychologicalTest.repository;

import com.cognisoftone.psychologicalTest.model.answers.TestAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestAnswerRepository extends JpaRepository<TestAnswer, Long> {

    List<TestAnswer> findByResponse_Test_IdAndResponse_User_Id(Long testId, Long userId);
}
