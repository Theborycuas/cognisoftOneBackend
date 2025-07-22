package com.cognisoftone.appointment.response;

import com.cognisoftone.appointment.model.AppointmentStatus;
import lombok.Data;

import java.sql.Time;
import java.time.LocalDateTime;

@Data
public class AppointmentResponse {
    private Long id;
    private Long patientId;
    private Long psychologistId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String reason;
    private String sessionNotes;
    private LocalDateTime DateTime;
    private Integer durationMinutes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AppointmentStatus status;
}

