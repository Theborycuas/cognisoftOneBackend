package com.cognisoftone.appointment.service;

import com.cognisoftone.appointment.interfaces.AppointmentService;
import com.cognisoftone.appointment.model.Appointment;
import com.cognisoftone.appointment.model.AppointmentStatus;
import com.cognisoftone.appointment.repository.AppointmentRepository;
import com.cognisoftone.appointment.request.CreateAppointmentRequest;
import com.cognisoftone.appointment.request.UpdateSessionNotesRequest;
import com.cognisoftone.appointment.response.AppointmentResponse;
import com.cognisoftone.common.exception.UnprocessableEntityException;
import com.cognisoftone.users.model.UserModel;
import com.cognisoftone.users.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    @Override
    public AppointmentResponse create(CreateAppointmentRequest request) {
        UserModel psychologist = userRepository.findById(request.getPsychologistId())
                .orElseThrow(() -> new EntityNotFoundException("Psychologist not found"));

        boolean isPsychologist = psychologist.getRoleModels().stream()
                .anyMatch(role -> role.getName().equals("PSYCHOLOGIST"));

        if (!isPsychologist) {
            throw new AccessDeniedException("User is not authorized as a psychologist");
        }

        // Validar que no sea en el pasado o dentro de 1 hora
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = request.getStartTime();

        if (startTime.isBefore(now.plusHours(1))) {
            throw new UnprocessableEntityException("Appointments must be scheduled at least 1 hour in advance.");
        }

        // Validar conflictos de horario
        List<Appointment> conflicts = appointmentRepository.findConflictingAppointments(
                request.getPsychologistId(),
                request.getPatientId(),
                request.getStartTime(),
                request.getEndTime()
        );

        if (!conflicts.isEmpty()) {
            throw new UnprocessableEntityException("Ya existe una cita agendada en este rango de tiempo.");
        }

        // Crear cita
        Appointment appointment = new Appointment();
        appointment.setPatientId(request.getPatientId());
        appointment.setPsychologistId(request.getPsychologistId());
        appointment.setStartTime(request.getStartTime());
        appointment.setEndTime(request.getEndTime());
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setReason(request.getReason());
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment.setDurationMinutes(request.getDurationMinutes());

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
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        appointment.setSessionNotes(request.getSessionNotes());
        appointment.setStatus(request.getStatus());

        Appointment updated = appointmentRepository.save(appointment);
        return mapToResponse(updated);
    }

    @Override
    public List<AppointmentResponse> getByPsychologistIdAndDateRange(Long psychologistId, LocalDateTime from, LocalDateTime to) {
        return appointmentRepository.findByPsychologistIdAndStartTimeBetween(psychologistId, from, to).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponse> getByPatientIdAndDateRange(Long patientId, LocalDateTime from, LocalDateTime to) {
        return appointmentRepository.findByPatientIdAndStartTimeBetween(patientId, from, to).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentResponse cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        appointment.setStatus(AppointmentStatus.CANCELED);
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
