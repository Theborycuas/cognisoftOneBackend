package com.cognisoftone.psychologicalTest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "test_assignment")
@NoArgsConstructor
@AllArgsConstructor
public class TestAssignment {

    @Id
    @GeneratedValue
    private Long id;

    private String token; // UUID único en el link
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private boolean filled;

    @ManyToOne
    private TestModel test;
}
