package com.cognisoftone.psychologicalTest.model.answers;

import com.cognisoftone.psychologicalTest.model.QuestionModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "test_answer")
@NoArgsConstructor
@AllArgsConstructor
public class TestAnswer {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private QuestionModel question;

    private String answer;

    @ManyToOne
    private TestResponse response;
}

