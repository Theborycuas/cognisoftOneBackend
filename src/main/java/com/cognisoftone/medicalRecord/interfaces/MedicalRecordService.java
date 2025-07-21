package com.cognisoftone.medicalRecord.interfaces;

import com.cognisoftone.medicalRecord.request.CreateMedicalRecordRequest;
import com.cognisoftone.medicalRecord.response.MedicalRecordResponse;
import com.cognisoftone.medicalRecord.response.PatientMedicalHistoryResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface MedicalRecordService {

    MedicalRecordResponse create(CreateMedicalRecordRequest request);

    List<MedicalRecordResponse> getByPatientId(Long patientId);

    List<MedicalRecordResponse> getByPsychologistId(Long psychologistId);

    List<MedicalRecordResponse> getByPatientAndDateRange(Long patientId, LocalDateTime from, LocalDateTime to);

    List<MedicalRecordResponse> getByPatientAndTest(Long patientId, Long testId);
}

