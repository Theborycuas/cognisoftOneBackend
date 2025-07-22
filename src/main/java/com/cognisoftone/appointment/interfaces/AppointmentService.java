package com.cognisoftone.appointment.interfaces;

import com.cognisoftone.appointment.request.CreateAppointmentRequest;
import com.cognisoftone.appointment.request.UpdateSessionNotesRequest;
import com.cognisoftone.appointment.response.AppointmentResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    AppointmentResponse create(CreateAppointmentRequest request);
    List<AppointmentResponse> getByPatientId(Long patientId);
    List<AppointmentResponse> getByPsychologistId(Long psychologistId);
    AppointmentResponse updateNotes(Long id, UpdateSessionNotesRequest request);
    List<AppointmentResponse> getByPsychologistIdAndDateRange(Long psychologistId, LocalDateTime from, LocalDateTime to);
    List<AppointmentResponse> getByPatientIdAndDateRange(Long patientId, LocalDateTime from, LocalDateTime to);
    AppointmentResponse cancelAppointment(Long id);

}

