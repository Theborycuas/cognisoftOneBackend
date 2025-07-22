package com.cognisoftone.appointment.model;


import jakarta.persistence.*;
import lombok.Data;

import java.sql.Time;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long patientId;

    private Long psychologistId;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String reason; // Motivo de consulta

    @Column(length = 5000)
    private String sessionNotes; // Notas de la sesión clínica

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status; // SCHEDULED, CANCELED, COMPLETED

    private Integer durationMinutes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}


