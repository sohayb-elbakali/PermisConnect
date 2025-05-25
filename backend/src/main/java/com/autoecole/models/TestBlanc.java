package com.autoecole.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tests_blancs")
public class TestBlanc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer dureeMinutes;

    @Column(nullable = false)
    private Integer nombreQuestions;

    @Column(nullable = false)
    private Integer scoreMinimum;

    @OneToMany(mappedBy = "testBlanc", cascade = CascadeType.ALL)
    private List<Question> questions;

    @OneToMany(mappedBy = "testBlanc")
    private List<ResultatTest> resultats;
} 