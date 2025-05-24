package com.perm.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private LocalDateTime dateCreation;
    private String type; // "ADMIN", "CLIENT" ou "MONITEUR"

    // Champs spécifiques selon le type d'utilisateur
    // Pour Admin
    private Long autoEcoleId;

    // Pour Moniteur
    private String specialite;
    private Integer experience;
    private String disponibilite;

    // Pour Client
    private Double progression;
    private LocalDateTime dateInscription;
    private String statut;
}
