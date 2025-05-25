package com.autoecole.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String enonce;

    @Column(nullable = false)
    private String reponseCorrecte;

    @ElementCollection
    @CollectionTable(name = "question_reponses", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "reponse")
    private List<String> reponsesPossibles;

    @Column(nullable = false)
    private Integer points;

    @Column(nullable = false)
    private Integer ordre;

    @ManyToOne
    @JoinColumn(name = "test_blanc_id", nullable = false)
    private TestBlanc testBlanc;
} 