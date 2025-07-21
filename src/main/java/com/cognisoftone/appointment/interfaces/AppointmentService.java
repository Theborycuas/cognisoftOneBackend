package com.cognisoftone.appointment.interfaces;

import com.cognisoftone.appointment.request.CreateAppointmentRequest;
import com.cognisoftone.appointment.request.UpdateSessionNotesRequest;
import com.cognisoftone.appointment.response.AppointmentResponse;

import java.util.List;

public interface AppointmentService {
    AppointmentResponse create(CreateAppointmentRequest request);
    List<AppointmentResponse> getByPatientId(Long patientId);
    List<AppointmentResponse> getByPsychologistId(Long psychologistId);
    AppointmentResponse updateNotes(Long id, UpdateSessionNotesRequest request);
}

