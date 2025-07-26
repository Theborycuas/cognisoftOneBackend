package com.cognisoftone.psychologicalTest.dto;

import java.time.LocalDate;

public record TestSummaryDTO(
        Long id,
        String name,
        String description,
        String category,
        boolean active,
        int questionCount,
        int estimatedTimeMinutes,
        LocalDate createdAt
) {}
