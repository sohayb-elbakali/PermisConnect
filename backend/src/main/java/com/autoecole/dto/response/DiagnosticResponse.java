package com.autoecole.dto.response;

import com.autoecole.models.Diagnostic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Réponse contenant les informations d'un diagnostic")
public class DiagnosticResponse {
    @Schema(description = "Identifiant unique du diagnostic", example = "1")
    private Long id;

    @Schema(description = "Identifiant du client", example = "1")
    private Long clientId;

    @Schema(description = "Nom complet du client", example = "John Doe")
    private String clientNom;

    @Schema(description = "Date et heure du diagnostic", example = "2024-03-20T14:30:00")
    private LocalDateTime dateDiagnostic;

    @Schema(description = "Nombre de tests passés par le client", example = "5")
    private Integer nombreTestsPasses;

    @Schema(description = "Moyenne des scores des tests", example = "75.5")
    private Double moyenneTests;

    @Schema(description = "Nombre d'heures de conduite effectuées", example = "20")
    private Integer nombreHeuresConduite;

    @Schema(description = "Niveau général du client", example = "Intermédiaire", allowableValues = {"Débutant", "Intermédiaire", "Avancé"})
    private String niveauGeneral;

    @Schema(description = "Commentaire détaillé sur le diagnostic", example = "Bon niveau. Quelques points à améliorer, mais vous êtes sur la bonne voie.")
    private String commentaire;

    public static DiagnosticResponse fromEntity(Diagnostic diagnostic) {
        DiagnosticResponse response = new DiagnosticResponse();
        response.setId(diagnostic.getId());
        response.setClientId(diagnostic.getClient().getId());
        response.setClientNom(diagnostic.getClient().getUser().getNom() + " " + diagnostic.getClient().getUser().getPrenom());
        response.setDateDiagnostic(diagnostic.getDateDiagnostic());
        response.setNombreTestsPasses(diagnostic.getNombreTestsPasses());
        response.setMoyenneTests(diagnostic.getMoyenneTests());
        response.setNombreHeuresConduite(diagnostic.getNombreHeuresConduite());
        response.setNiveauGeneral(diagnostic.getNiveauGeneral());
        response.setCommentaire(diagnostic.getCommentaire());
        return response;
    }
} 