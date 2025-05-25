package com.autoecole.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "diagnostics")
public class Diagnostic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(nullable = false)
    private LocalDateTime dateDiagnostic;

    @Column(nullable = false)
    private Integer nombreTestsPasses;

    @Column(nullable = false)
    private Double moyenneTests;

    @Column(nullable = false)
    private Integer nombreHeuresConduite;

    @Column(nullable = false)
    private String niveauGeneral; // Débutant, Intermédiaire, Avancé

    @Column(nullable = false, length = 1000)
    private String commentaire;
} 