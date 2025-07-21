package com.cognisoftone.psychologicalTest.model.answers;

import com.cognisoftone.psychologicalTest.model.TestModel;
import com.cognisoftone.users.model.UserModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "test_response")
@NoArgsConstructor
@AllArgsConstructor
public class TestResponse {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime submittedAt;

    @ManyToOne
    private UserModel user;

    @ManyToOne
    private TestModel test;

    @OneToMany(mappedBy = "response", cascade = CascadeType.ALL)
    private List<TestAnswer> answers;
}

