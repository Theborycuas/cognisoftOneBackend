package com.cognisoftone.psychologicalTest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "questions")
@NoArgsConstructor
@AllArgsConstructor
public class QuestionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String statement;
    private String type; // FREE_TEXT, SINGLE_CHOICE, MULTIPLE_CHOICE

    @ElementCollection
    private List<String> options;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private TestModel test;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

}

