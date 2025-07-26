package com.cognisoftone.users.service;

import com.cognisoftone.users.dto.PatientComboDTO;
import com.cognisoftone.users.interfaces.UserService;
import com.cognisoftone.users.model.UserModel;
import com.cognisoftone.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<PatientComboDTO> getPatientsOfPsychologist(Long psychologistId) {
        List<UserModel> patients = userRepository.findPatientsOfPsychologist(psychologistId);

        return patients.stream().map(p -> new PatientComboDTO(
                p.getId(),
                p.getFirstName() + " " + p.getLastName(),
                p.getIdentification(),
                p.getEmail()
        )).toList();
    }

}
