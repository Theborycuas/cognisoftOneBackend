package com.cognisoftone.medicalRecord.service;

import com.cognisoftone.appointment.model.Appointment;
import com.cognisoftone.appointment.repository.AppointmentRepository;
import com.cognisoftone.appointment.response.AppointmentResponse;
import com.cognisoftone.medicalRecord.dto.MedicalEventDTO;
import com.cognisoftone.medicalRecord.dto.PatientBasicInfoDTO;
import com.cognisoftone.medicalRecord.interfaces.MedicalHistoryService;
import com.cognisoftone.medicalRecord.model.MedicalRecord;
import com.cognisoftone.medicalRecord.repository.MedicalRecordRepository;
import com.cognisoftone.medicalRecord.response.MedicalRecordResponse;
import com.cognisoftone.medicalRecord.response.PatientMedicalHistoryResponse;
import com.cognisoftone.psychologicalTest.model.TestResult;
import com.cognisoftone.psychologicalTest.repository.TestResultRepository;
import com.cognisoftone.psychologicalTest.response.TestResultResponse;
import com.cognisoftone.users.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalHistoryServiceImpl implements MedicalHistoryService {

    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final TestResultRepository testResultRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    @Override
    public PatientMedicalHistoryResponse getFullHistoryByPatientId(Long patientId) {

        var user = userRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient with ID " + patientId + " was not found."));


        // Armar datos del paciente
        PatientBasicInfoDTO patient = new PatientBasicInfoDTO();
        patient.setId(user.getId());
        patient.setFirstName(user.getFirstName());
        patient.setLastName(user.getLastName());
        patient.setIdentification(user.getIdentification());
        patient.setEmail(user.getEmail());
        patient.setPhone(user.getPhone());
        patient.setConsent(user.getConsent());

        // Recoger registros clínicos
        List<MedicalRecord> records = medicalRecordRepository.findByPatientId(patientId);

        // Armar eventos clínicos
        List<MedicalEventDTO> events = records.stream()
                .map(record -> {
                    MedicalEventDTO event = new MedicalEventDTO();
                    event.setDate(record.getCreatedAt());

                    if (record.getAppointmentId() != null) {
                        event.setType("CITA");
                        appointmentRepository.findById(record.getAppointmentId())
                                .ifPresent(app -> {
                                    event.setAppointment(mapToAppointmentResponse(app));
                                });
                    } else if (record.getTestId() != null) {
                        event.setType("TEST");
                        testResultRepository.findByTestIdAndPatientId(record.getTestId(), patientId)
                                .ifPresent(test -> {
                                    event.setTestResult(mapToTestResultResponse(test));
                                });
                    } else {
                        event.setType("OBSERVACIÓN");
                    }

                    event.setMedicalRecord(mapToMedicalRecordResponse(record));
                    return event;
                })
                .sorted(Comparator.comparing(MedicalEventDTO::getDate))
                .collect(Collectors.toList());

        // Respuesta final
        PatientMedicalHistoryResponse response = new PatientMedicalHistoryResponse();
        response.setPatient(patient);
        response.setMedicalEvents(events);

        return response;
    }

    private AppointmentResponse mapToAppointmentResponse(Appointment appointment) {
        AppointmentResponse dto = new AppointmentResponse();
        dto.setId(appointment.getId());
        dto.setPatientId(appointment.getPatientId());
        dto.setPsychologistId(appointment.getPsychologistId());
        dto.setDurationMinutes(appointment.getDurationMinutes());
        dto.setStatus(appointment.getStatus());
        dto.setSessionNotes(appointment.getSessionNotes());
        dto.setCreatedAt(appointment.getCreatedAt());
        dto.setUpdatedAt(appointment.getUpdatedAt());
        return dto;
    }

    private TestResultResponse mapToTestResultResponse(TestResult result) {
        TestResultResponse dto = new TestResultResponse();
        dto.setId(result.getId());
        dto.setTestId(result.getTestId());
        dto.setPatientId(result.getPatientId());
        dto.setAutoSummary(result.getAutoSummary());
        dto.setObservations(result.getObservations());
        dto.setCompletedAt(result.getCompletedAt());
        dto.setCreatedAt(result.getCreatedAt());
        dto.setUpdatedAt(result.getUpdatedAt());
        return dto;
    }

    private MedicalRecordResponse mapToMedicalRecordResponse(MedicalRecord record) {
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
