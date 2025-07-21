package com.cognisoftone.appointment.response;

import com.cognisoftone.appointment.model.AppointmentStatus;
import lombok.Data;

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
    private AppointmentStatus status;
}

