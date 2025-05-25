package com.autoecole.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CoursResponse {
    private Long id;
    private String titre;
    private String description;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private Integer capaciteMax;
    private Double prix;
    private String type; // PRIVE or PUBLIC
    private String niveau;
    private Long moniteurId;
    private String moniteurNom;
    private Long autoEcoleId;
    private String autoEcoleNom;
} 