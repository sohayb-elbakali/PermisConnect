package com.autoecole.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "cours")
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type"
)
@JsonSubTypes({
  @JsonSubTypes.Type(value = CoursPublic.class, name = "PUBLIC"),
  @JsonSubTypes.Type(value = CoursPrive.class, name = "PRIVATE")
})
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

    @Column
    private String fileType;

    @ManyToOne
    @JoinColumn(name = "moniteur_id")
    private Moniteur moniteur;
} 
