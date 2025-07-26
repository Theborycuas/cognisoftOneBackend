package com.cognisoftone.users.interfaces;

import com.cognisoftone.users.dto.PatientComboDTO;

import java.util.List;

public interface UserService {

    List<PatientComboDTO> getPatientsOfPsychologist(Long psychologistId);
}
