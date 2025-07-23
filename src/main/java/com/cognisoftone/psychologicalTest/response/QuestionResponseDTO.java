package com.cognisoftone.psychologicalTest.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResponseDTO {
    private Long id;
    private String statement;
    private String type; // FREE_TEXT, SINGLE_CHOICE, MULTIPLE_CHOICE
    private List<String> options;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
