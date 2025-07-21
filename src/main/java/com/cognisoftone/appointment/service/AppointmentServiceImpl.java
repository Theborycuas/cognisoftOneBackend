package com.cognisoftone.appointment.service;

import com.cognisoftone.appointment.interfaces.AppointmentService;
import com.cognisoftone.appointment.model.Appointment;
import com.cognisoftone.appointment.model.AppointmentStatus;
import com.cognisoftone.appointment.repository.AppointmentRepository;
import com.cognisoftone.appointment.request.CreateAppointmentRequest;
import com.cognisoftone.appointment.request.UpdateSessionNotesRequest;
import com.cognisoftone.appointment.response.AppointmentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Override
    public AppointmentResponse create(CreateAppointmentRequest request) {
        Appointment appointment = new Appointment();
        appointment.setPatientId(request.getPatientId());
        appointment.setPsychologistId(request.getPsychologistId());
        appointment.setStartTime(request.getStartTime());
        appointment.setEndTime(request.getEndTime());
        appointment.setReason(request.getReason());
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        Appointment saved = appointmentRepository.save(appointment);
        return mapToResponse(saved);
    }

    @Override
    public List<AppointmentResponse> getByPatientId(Long patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponse> getByPsychologistId(Long psychologistId) {
        return appointmentRepository.findByPsychologistId(psychologistId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentResponse updateNotes(Long id, UpdateSessionNotesRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setSessionNotes(request.getSessionNotes());
        appointment.setStatus(request.getStatus());

        Appointment updated = appointmentRepository.save(appointment);
        return mapToResponse(updated);
    }

    // Mapeo manual de entidad -> DTO
    private AppointmentResponse mapToResponse(Appointment appointment) {
        AppointmentResponse response = new AppointmentResponse();
        response.setId(appointment.getId());
        response.setPatientId(appointment.getPatientId());
        response.setPsychologistId(appointment.getPsychologistId());
        response.setStartTime(appointment.getStartTime());
        response.setEndTime(appointment.getEndTime());
        response.setReason(appointment.getReason());
        response.setSessionNotes(appointment.getSessionNotes());
        response.setStatus(appointment.getStatus());
        return response;
    }
}
