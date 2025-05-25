package com.autoecole.controllers;

import com.autoecole.dto.response.DiagnosticResponse;
import com.autoecole.models.Diagnostic;
import com.autoecole.services.DiagnosticService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/diagnostics")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
@Tag(name = "Diagnostics", description = "API pour la gestion des diagnostics des clients")
public class DiagnosticController {
    private final DiagnosticService diagnosticService;

    @Operation(
        summary = "Générer un nouveau diagnostic",
        description = "Génère un nouveau diagnostic pour un client spécifique basé sur ses résultats de tests"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Diagnostic généré avec succès",
            content = @Content(schema = @Schema(implementation = DiagnosticResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Client non trouvé"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Aucun test trouvé pour le client"
        )
    })
    @PostMapping("/client/{clientId}")
    public ResponseEntity<DiagnosticResponse> generateDiagnostic(
            @Parameter(description = "ID du client", required = true)
            @PathVariable Long clientId) {
        Diagnostic diagnostic = diagnosticService.generateDiagnostic(clientId);
        return ResponseEntity.ok(DiagnosticResponse.fromEntity(diagnostic));
    }

    @Operation(
        summary = "Obtenir tous les diagnostics d'un client",
        description = "Récupère l'historique complet des diagnostics d'un client spécifique"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Liste des diagnostics récupérée avec succès",
            content = @Content(schema = @Schema(implementation = DiagnosticResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Client non trouvé"
        )
    })
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<DiagnosticResponse>> getClientDiagnostics(
            @Parameter(description = "ID du client", required = true)
            @PathVariable Long clientId) {
        List<Diagnostic> diagnostics = diagnosticService.getClientDiagnostics(clientId);
        List<DiagnosticResponse> responses = diagnostics.stream()
                .map(DiagnosticResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Operation(
        summary = "Obtenir le dernier diagnostic d'un client",
        description = "Récupère le diagnostic le plus récent d'un client spécifique"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Dernier diagnostic récupéré avec succès",
            content = @Content(schema = @Schema(implementation = DiagnosticResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Client non trouvé ou aucun diagnostic disponible"
        )
    })
    @GetMapping("/client/{clientId}/latest")
    public ResponseEntity<DiagnosticResponse> getLatestDiagnostic(
            @Parameter(description = "ID du client", required = true)
            @PathVariable Long clientId) {
        Diagnostic diagnostic = diagnosticService.getLatestDiagnostic(clientId);
        return ResponseEntity.ok(DiagnosticResponse.fromEntity(diagnostic));
    }
} 