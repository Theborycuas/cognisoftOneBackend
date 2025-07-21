package com.cognisoftone.psychologicalTest.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignTestRequest {
    private Long testId;
    private int validDays; // días de validez del link

}
