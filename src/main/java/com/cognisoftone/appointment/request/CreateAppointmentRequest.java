package com.cognisoftone.appointment.request;

import lombok.Data;

import java.sql.Time;
import java.time.LocalDateTime;

@Data
public class CreateAppointmentRequest {
    private Long patientId;
    private Long psychologistId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String reason;
    private Integer durationMinutes;
}

