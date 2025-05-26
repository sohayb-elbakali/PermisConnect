package com.autoecole.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "cours")
public abstract class Cours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private LocalDateTime dateDebut;

    @Column(nullable = false)
    private LocalDateTime dateFin;

    @Column(nullable = false)
    private Integer capaciteMax;

    @Column(nullable = false)
    private Double prix;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CourseType courseType;

    @Column
    private String cloudinaryUrl;

    @ManyToOne
    @JoinColumn(name = "moniteur_id")
    private Moniteur moniteur;
} 