package com.cognisoftone.psychologicalTest.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
public class AssignTestResponse {
    private String link;
    private LocalDateTime expiresAt;

    public AssignTestResponse(String link, LocalDateTime expiresAt) {
        this.link = link;
        this.expiresAt = expiresAt;
    }

}

