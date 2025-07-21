package com.cognisoftone.medicalRecord.repository;

import com.cognisoftone.medicalRecord.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    List<MedicalRecord> findByPatientId(Long patientId);

    List<MedicalRecord> findByPsychologistId(Long psychologistId);

    List<MedicalRecord> findByPatientIdAndCreatedAtBetween(Long patientId, LocalDateTime from, LocalDateTime to);

    List<MedicalRecord> findByPatientIdAndTestId(Long patientId, Long testId);
}

