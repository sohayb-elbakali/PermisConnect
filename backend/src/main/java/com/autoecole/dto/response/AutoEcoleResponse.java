package com.autoecole.dto.response;

import com.autoecole.models.AutoEcole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Données de réponse pour une auto-école")
public class AutoEcoleResponse {
    @Schema(description = "ID de l'auto-école", example = "1")
    private Long id;

    @Schema(description = "Nom de l'auto-école", example = "Auto-École Excellence")
    private String nom;

    @Schema(description = "Email de l'auto-école", example = "contact@auto-ecole-excellence.com")
    private String email;

    @Schema(description = "Numéro de téléphone de l'auto-école", example = "+33123456789")
    private String telephone;

    @Schema(description = "Adresse de l'auto-école", example = "123 avenue de la République")
    private String adresse;

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

    public static AutoEcoleResponse fromEntity(AutoEcole autoEcole) {
        AutoEcoleResponse response = new AutoEcoleResponse();
        response.setId(autoEcole.getId());
        response.setNom(autoEcole.getNom());
        response.setEmail(autoEcole.getEmail());
        response.setTelephone(autoEcole.getTelephone());
        response.setAdresse(autoEcole.getAdresse());
        response.setSiret(autoEcole.getSiret());
        response.setCodePostal(autoEcole.getCodePostal());
        response.setVille(autoEcole.getVille());
        response.setSiteWeb(autoEcole.getSiteWeb());
        response.setDescription(autoEcole.getDescription());
        response.setHoraires(autoEcole.getHoraires());
        return response;
    }
} 