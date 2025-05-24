package com.perm.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CoursDTO {
    private Long id;
    private String titre;
    private String description;
    private Integer duree;
    private boolean actif;
    private String type; // "PRIVE" ou "PUBLIC"
    private Long autoEcoleId;
    private String autoEcoleNom;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private String creePar;
    private String modifiePar;
}