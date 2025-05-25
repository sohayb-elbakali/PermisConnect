package com.autoecole.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Données de requête pour la création/mise à jour d'un client")
public class ClientRequest {
    @NotBlank(message = "Le nom est obligatoire")
    @Schema(description = "Nom du client", example = "Doe")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Schema(description = "Prénom du client", example = "John")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    @Schema(description = "Email du client", example = "john.doe@example.com")
    private String email;

    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Format de numéro de téléphone invalide")
    @Schema(description = "Numéro de téléphone du client", example = "+33612345678")
    private String telephone;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    @Schema(description = "Mot de passe du client", example = "password123")
    private String password;

    @Schema(description = "Adresse du client", example = "123 rue de Paris")
    private String adresse;

    @Schema(description = "Date de naissance du client", example = "1990-01-01")
    private String dateNaissance;

    @Schema(description = "Numéro de permis du client", example = "123456789")
    private String numeroPermis;

    @Schema(description = "Type de permis du client", example = "B")
    private String typePermis;
} 