package com.cognisoftone.appointment.repository;

import com.cognisoftone.appointment.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPsychologistId(Long psychologistId);

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByPsychologistIdAndStartTimeBetween(
            Long psychologistId, LocalDateTime from, LocalDateTime to
    );

    List<Appointment> findByPatientIdAndStartTimeBetween(
            Long patientId, LocalDateTime from, LocalDateTime to
    );
}

