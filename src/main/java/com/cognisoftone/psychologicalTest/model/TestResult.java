package com.cognisoftone.psychologicalTest.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "test_results")
public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long testId;          // TestModel.id
    private Long patientId;       // Usuario que lo llenó
    private LocalDateTime completedAt;

    @Column(length = 5000)
    private String autoSummary;   // Un resumen automático generado por IA o lógica

    @Column(length = 10000)
    private String observations;  // Notas manuales (opcional)

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.completedAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

