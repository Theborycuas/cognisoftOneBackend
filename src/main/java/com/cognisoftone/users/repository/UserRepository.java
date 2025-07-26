package com.cognisoftone.users.repository;

import com.cognisoftone.users.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByIdentification(String identification);
    Optional<UserModel> findByIdentification(String identification);

    @Query("""
    SELECT u FROM UserModel u
    WHERE u.id IN (
        SELECT DISTINCT a.patientId
        FROM Appointment a
        WHERE a.psychologistId = :psychologistId
    )
    """)
    List<UserModel> findPatientsOfPsychologist(@Param("psychologistId") Long psychologistId);
}
