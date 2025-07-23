package com.cognisoftone.psychologicalTest.model.answers;

import com.cognisoftone.psychologicalTest.model.TestModel;
import com.cognisoftone.users.model.UserModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "test_response")
@NoArgsConstructor
@AllArgsConstructor
public class TestResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserModel user;

    @ManyToOne
    private TestModel test;

    private LocalDateTime submittedAt;

    @OneToMany(mappedBy = "response", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestAnswer> answers = new ArrayList<>();
}

