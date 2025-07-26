package com.cognisoftone.users.dto;

public record PatientComboDTO(
        Long id,
        String fullName,
        String identification, // cédula
        String email
) {}

