package com.cognisoftone.medicalRecord.interfaces;

import com.cognisoftone.medicalRecord.request.CreateMedicalRecordRequest;
import com.cognisoftone.medicalRecord.response.MedicalRecordResponse;
import com.cognisoftone.medicalRecord.response.PatientMedicalHistoryResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface MedicalHistoryService {

    PatientMedicalHistoryResponse getFullHistoryByPatientId(Long patientId);
}

