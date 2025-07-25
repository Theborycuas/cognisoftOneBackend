package com.cognisoftone.medicalRecord.service;

import com.cognisoftone.appointment.repository.AppointmentRepository;
import com.cognisoftone.common.exception.DuplicateResourceException;
import com.cognisoftone.medicalRecord.interfaces.MedicalRecordService;
import com.cognisoftone.medicalRecord.model.MedicalRecord;
import com.cognisoftone.medicalRecord.repository.MedicalRecordRepository;
import com.cognisoftone.medicalRecord.request.CreateMedicalRecordRequest;
import com.cognisoftone.medicalRecord.response.MedicalRecordResponse;
import com.cognisoftone.users.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public MedicalRecordResponse create(CreateMedicalRecordRequest request) {

        // Validar existencia del paciente
        userRepository.findById(request.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("El paciente no existe."));

        // Validar existencia del psicólogo
        userRepository.findById(request.getPsychologistId())
                .orElseThrow(() -> new EntityNotFoundException("El psicólogo no existe."));

        // Validar existencia de la cita (si se envía)
        if (request.getAppointmentId() != null) {
            appointmentRepository.findById(request.getAppointmentId())
                    .orElseThrow(() -> new EntityNotFoundException("La cita no existe."));
        }

        // Validar duplicado por paciente + psicólogo + cita
        boolean alreadyExists = medicalRecordRepository
                .existsByPatientIdAndPsychologistIdAndAppointmentId(
                        request.getPatientId(),
                        request.getPsychologistId(),
                        request.getAppointmentId()
                );

        if (alreadyExists) {
            throw new DuplicateResourceException("Ya existe una historia clínica registrada para esta cita.");
        }

        // Crear el registro clínico
        MedicalRecord record = new MedicalRecord();
        record.setPatientId(request.getPatientId());
        record.setPsychologistId(request.getPsychologistId());
        record.setAppointmentId(request.getAppointmentId());
        record.setTestId(request.getTestId());
        record.setContext(request.getContext());
        record.setFindings(request.getFindings());
        record.setDiagnosis(request.getDiagnosis());
        record.setRecommendations(request.getRecommendations());
        record.setPsychologistNotes(request.getPsychologistNotes());
        record.setReviewed(request.getReviewed());

        MedicalRecord saved = medicalRecordRepository.save(record);
        return mapToResponse(saved);
    }



    @Override
    public List<MedicalRecordResponse> getByPatientId(Long patientId) {
        return medicalRecordRepository.findByPatientId(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalRecordResponse> getByPsychologistId(Long psychologistId) {
        return medicalRecordRepository.findByPsychologistId(psychologistId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalRecordResponse> getByPatientAndDateRange(Long patientId, LocalDateTime from, LocalDateTime to) {
        return medicalRecordRepository.findByPatientIdAndCreatedAtBetween(patientId, from, to).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalRecordResponse> getByPatientAndTest(Long patientId, Long testId) {
        return medicalRecordRepository.findByPatientIdAndTestId(patientId, testId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private MedicalRecordResponse mapToResponse(MedicalRecord record) {
        MedicalRecordResponse res = new MedicalRecordResponse();
        res.setId(record.getId());
        res.setPatientId(record.getPatientId());
        res.setPsychologistId(record.getPsychologistId());
        res.setAppointmentId(record.getAppointmentId());
        res.setTestId(record.getTestId());
        res.setContext(record.getContext());
        res.setFindings(record.getFindings());
        res.setDiagnosis(record.getDiagnosis());
        res.setRecommendations(record.getRecommendations());
        res.setPsychologistNotes(record.getPsychologistNotes());
        res.setReviewed(record.getReviewed());
        res.setCreatedAt(record.getCreatedAt());
        res.setUpdatedAt(record.getUpdatedAt());
        return res;
    }
}

