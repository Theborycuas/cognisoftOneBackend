package com.cognisoftone.psychologicalTest.repository;

import com.cognisoftone.psychologicalTest.model.TestModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<TestModel, Long> {

    List<TestModel> findByActiveTrue();

}
