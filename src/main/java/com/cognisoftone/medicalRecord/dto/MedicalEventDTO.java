package com.cognisoftone.medicalRecord.dto;

import com.cognisoftone.appointment.response.AppointmentResponse;
import com.cognisoftone.medicalRecord.response.MedicalRecordResponse;
import com.cognisoftone.psychologicalTest.response.TestResultResponse;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MedicalEventDTO {
    private LocalDateTime date;
    private String type; // "CITA", "TEST", "OBSERVACIÓN"
    private AppointmentResponse appointment;
    private TestResultResponse testResult;
    private MedicalRecordResponse medicalRecord;
}

