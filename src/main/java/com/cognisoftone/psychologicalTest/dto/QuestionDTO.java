package com.cognisoftone.psychologicalTest.dto;

import java.util.List;

public record QuestionDTO(
        Long id,
        String statement,
        String type,
        List<String> options
) {}
