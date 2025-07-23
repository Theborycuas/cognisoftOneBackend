package com.cognisoftone.medicalRecord.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "medical_records")
@Getter
@Setter
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long patientId;
    private Long psychologistId;

    private Long appointmentId; // nullable si fue test externo
    private Long testId;        // nullable si no hubo test

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String context;           // antecedentes
    private String findings;          // síntomas o hallazgos
    private String diagnosis;         // diagnóstico clínico
    private String recommendations;   // plan de acción
    private String psychologistNotes; // notas personales del profesional
    private Boolean reviewed;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
