package com.autoecole.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "resultats_tests")
public class ResultatTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "test_blanc_id", nullable = false)
    private TestBlanc testBlanc;

    @Column(nullable = false)
    private LocalDateTime datePassage;

    @Column(nullable = false)
    private Integer score;

    @Column(nullable = false)
    private Boolean reussi;

    @Column(nullable = false)
    private Integer tempsUtilise; // en minutes
} 