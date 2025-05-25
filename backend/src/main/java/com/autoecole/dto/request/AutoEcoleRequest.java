package com.autoecole.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Données de requête pour la création/mise à jour d'une auto-école")
public class AutoEcoleRequest {
    @NotBlank(message = "Le nom est obligatoire")
    @Schema(description = "Nom de l'auto-école", example = "Auto-École Excellence")
    private String nom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    @Schema(description = "Email de l'auto-école", example = "contact@auto-ecole-excellence.com")
    private String email;

    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Format de numéro de téléphone invalide")
    @Schema(description = "Numéro de téléphone de l'auto-école", example = "+33123456789")
    private String telephone;

    @NotBlank(message = "L'adresse est obligatoire")
    @Schema(description = "Adresse de l'auto-école", example = "123 avenue de la République")
    private String adresse;

    @NotBlank(message = "Le numéro SIRET est obligatoire")
    @Pattern(regexp = "^[0-9]{14}$", message = "Le numéro SIRET doit contenir exactement 14 chiffres")
    @Schema(description = "Numéro SIRET de l'auto-école", example = "12345678901234")
    private String siret;

    @Schema(description = "Code postal de l'auto-école", example = "75011")
    private String codePostal;

    @Schema(description = "Ville de l'auto-école", example = "Paris")
    private String ville;

    @Schema(description = "Site web de l'auto-école", example = "https://www.auto-ecole-excellence.com")
    private String siteWeb;

    @Schema(description = "Description de l'auto-école", example = "Auto-école proposant des formations de qualité")
    private String description;

    @Schema(description = "Horaires d'ouverture", example = "Lundi-Vendredi: 9h-19h, Samedi: 9h-17h")
    private String horaires;
} 