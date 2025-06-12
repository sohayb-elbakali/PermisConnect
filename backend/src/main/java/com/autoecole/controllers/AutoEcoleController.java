package com.autoecole.controllers;

import com.autoecole.dto.request.AutoEcoleRequest;
import com.autoecole.dto.response.AutoEcoleResponse;
import com.autoecole.models.AutoEcole;
import com.autoecole.services.AutoEcoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.NoArgsConstructor;

@RestController
@RequestMapping("/api/auto-ecoles")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
@Tag(name = "Auto-écoles", description = "API pour la gestion des auto-écoles")
public class AutoEcoleController {
    private final AutoEcoleService autoEcoleService;

    @Operation(summary = "Créer une nouvelle auto-école")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Auto-école créée avec succès",
            content = @Content(schema = @Schema(implementation = AutoEcoleResponse.class))),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "409", description = "Email ou téléphone déjà utilisé")
    })
    @PostMapping
    public ResponseEntity<AutoEcoleResponse> createAutoEcole(@Valid @RequestBody AutoEcoleRequest request) {
        try {
            AutoEcole autoEcole = autoEcoleService.createAutoEcole(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(AutoEcoleResponse.fromEntity(autoEcole));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Operation(summary = "Obtenir toutes les auto-écoles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des auto-écoles récupérée avec succès",
            content = @Content(schema = @Schema(implementation = AutoEcoleResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<AutoEcoleResponse>> getAllAutoEcoles() {
        List<AutoEcole> autoEcoles = autoEcoleService.getAllAutoEcoles();
        List<AutoEcoleResponse> responses = autoEcoles.stream()
                .map(AutoEcoleResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Obtenir une auto-école par son ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Auto-école récupérée avec succès",
            content = @Content(schema = @Schema(implementation = AutoEcoleResponse.class))),
        @ApiResponse(responseCode = "404", description = "Auto-école non trouvée")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AutoEcoleResponse> getAutoEcoleById(
            @Parameter(description = "ID de l'auto-école", required = true)
            @PathVariable Long id) {
        try {
            AutoEcole autoEcole = autoEcoleService.getAutoEcoleById(id);
            return ResponseEntity.ok(AutoEcoleResponse.fromEntity(autoEcole));
        } catch (jakarta.persistence.EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Mettre à jour une auto-école")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Auto-école mise à jour avec succès",
            content = @Content(schema = @Schema(implementation = AutoEcoleResponse.class))),
        @ApiResponse(responseCode = "404", description = "Auto-école non trouvée"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "409", description = "Email ou téléphone déjà utilisé")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AutoEcoleResponse> updateAutoEcole(
            @Parameter(description = "ID de l'auto-école", required = true)
            @PathVariable Long id,
            @Valid @RequestBody AutoEcoleRequest request) {
        try {
            AutoEcole autoEcole = autoEcoleService.updateAutoEcole(id, request);
            return ResponseEntity.ok(AutoEcoleResponse.fromEntity(autoEcole));
        } catch (jakarta.persistence.EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Operation(summary = "Supprimer une auto-école")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Auto-école supprimée avec succès"),
        @ApiResponse(responseCode = "404", description = "Auto-école non trouvée")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAutoEcole(
            @Parameter(description = "ID de l'auto-école", required = true)
            @PathVariable Long id) {
        try {
            autoEcoleService.deleteAutoEcole(id);
            return ResponseEntity.noContent().build();
        } catch (jakarta.persistence.EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Rechercher des auto-écoles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recherche effectuée avec succès",
            content = @Content(schema = @Schema(implementation = AutoEcoleResponse.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<List<AutoEcoleResponse>> searchAutoEcoles(
            @Parameter(description = "Terme de recherche")
            @RequestParam(required = false) String query) {
        List<AutoEcole> autoEcoles = autoEcoleService.searchAutoEcoles(query);
        List<AutoEcoleResponse> responses = autoEcoles.stream()
                .map(AutoEcoleResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    @Operation(summary = "Obtenir les plans d'abonnement disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Plans récupérés avec succès",
            content = @Content(schema = @Schema(implementation = SubscriptionPlanResponse.class)))
    })
    @GetMapping("/{id}/subscription-plans")
    public ResponseEntity<List<SubscriptionPlanResponse>> getSubscriptionPlans(
            @Parameter(description = "ID de l'auto-école")
            @PathVariable Long id) {
        // Vérifier que l'auto-école existe
        AutoEcole autoEcole = autoEcoleService.getAutoEcoleById(id);
        if (autoEcole == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Créer les plans d'abonnement (statiques pour l'instant)
        List<SubscriptionPlanResponse> plans = new ArrayList<>();
        
        // Plan gratuit
        SubscriptionPlanResponse freePlan = new SubscriptionPlanResponse();
        freePlan.setId("free");
        freePlan.setName("Plan Gratuit");
        freePlan.setDescription("Accès limité aux cours de base");
        freePlan.setPrice(0.0);
        freePlan.setFeatures(Arrays.asList(
            "Accès aux cours gratuits",
            "Réservation de leçons limitée",
            "Support par email"
        ));
        plans.add(freePlan);
        
        // Plan premium
        SubscriptionPlanResponse premiumPlan = new SubscriptionPlanResponse();
        premiumPlan.setId("premium");
        premiumPlan.setName("Plan Premium");
        premiumPlan.setDescription("Accès complet à tous les cours et fonctionnalités");
        premiumPlan.setPrice(19.99);
        premiumPlan.setFeatures(Arrays.asList(
            "Accès à TOUS les cours (y compris premium)",
            "Réservation de leçons illimitée",
            "Support prioritaire",
            "Matériel pédagogique exclusif",
            "Tests blancs illimités"
        ));
        plans.add(premiumPlan);
        
        return ResponseEntity.ok(plans);
    }
    
    // Classe de réponse pour les plans d'abonnement
    @Data
    @NoArgsConstructor
    public static class SubscriptionPlanResponse {
        private String id;
        private String name;
        private String description;
        private Double price;
        private List<String> features;
    }
}