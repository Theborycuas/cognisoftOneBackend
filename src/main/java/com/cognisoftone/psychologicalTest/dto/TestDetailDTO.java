package com.cognisoftone.psychologicalTest.dto;

import java.util.List;

public record TestDetailDTO(
        Long id,
        String name,
        String description,
        List<QuestionDTO> questions
) {}
