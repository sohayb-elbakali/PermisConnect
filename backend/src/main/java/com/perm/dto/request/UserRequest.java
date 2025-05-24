package com.perm.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;



@Data
public class UserRequest {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String motDePasse;

    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;

    // Champs spécifiques pour Admin
    private Long autoEcoleId;

    // Champs spécifiques pour Moniteur
    private String specialite;
    private Integer experience;
    private String disponibilite;

    // Champs spécifiques pour Client
    private String statut;
}