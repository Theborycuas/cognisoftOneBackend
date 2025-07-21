package com.cognisoftone.medicalRecord.response;

import com.cognisoftone.medicalRecord.dto.MedicalEventDTO;
import com.cognisoftone.medicalRecord.dto.PatientBasicInfoDTO;
import lombok.Data;

import java.util.List;

@Data
public class PatientMedicalHistoryResponse {
    private PatientBasicInfoDTO patient;
    private List<MedicalEventDTO> medicalEvents;
}
