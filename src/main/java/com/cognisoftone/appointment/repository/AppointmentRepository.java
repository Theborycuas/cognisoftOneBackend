package com.cognisoftone.appointment.repository;

import com.cognisoftone.appointment.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("""
    SELECT a FROM Appointment a
    WHERE a.status = 'SCHEDULED'
      AND (
          (a.psychologistId = :psychologistId OR a.patientId = :patientId)
          AND (
              (:startTime BETWEEN a.startTime AND a.endTime) OR
              (:endTime BETWEEN a.startTime AND a.endTime) OR
              (a.startTime BETWEEN :startTime AND :endTime)
          )
    )
    """)
    List<Appointment> findConflictingAppointments(
            @Param("psychologistId") Long psychologistId,
            @Param("patientId") Long patientId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

}

