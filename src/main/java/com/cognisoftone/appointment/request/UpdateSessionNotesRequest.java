package com.cognisoftone.appointment.request;

import com.cognisoftone.appointment.model.AppointmentStatus;
import lombok.Data;

@Data
public class UpdateSessionNotesRequest {
    private String sessionNotes;
    private AppointmentStatus status;
}

