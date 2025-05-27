package com.autoecole.controllers;

import com.autoecole.dto.CreateMoniteurRequest;
import com.autoecole.dto.MoniteurDTO;
import com.autoecole.models.Moniteur;
import com.autoecole.services.MoniteurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/moniteurs")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
@Tag(name = "Moniteur Management", description = "Operations related to driving instructors")
public class MoniteurController {
    private final MoniteurService moniteurService;

    @Operation(summary = "Get all moniteurs", description = "Retrieves a list of all driving instructors")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all moniteurs"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<Moniteur>> getAllMoniteurs() {
        return ResponseEntity.ok(moniteurService.getAllMoniteurs());
    }

    @Operation(summary = "Get moniteur by ID", description = "Retrieves a specific moniteur by their ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the moniteur"),
        @ApiResponse(responseCode = "404", description = "Moniteur not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Moniteur> getMoniteurById(
            @Parameter(description = "ID of the moniteur to retrieve") @PathVariable Long id) {
        return moniteurService.getMoniteurById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Moniteur> getMoniteurByEmail(@PathVariable String email) {
        return moniteurService.getMoniteurByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/auto-ecole/{autoEcoleId}")
    public ResponseEntity<List<MoniteurDTO>> getMoniteursByAutoEcole(@PathVariable Long autoEcoleId) {
        List<MoniteurDTO> dtos = moniteurService.getMoniteursByAutoEcole(autoEcoleId)
            .stream()
            .map(moniteur -> {
                MoniteurDTO dto = new MoniteurDTO();
                dto.setId(moniteur.getId());
                dto.setNom(moniteur.getNom());
                dto.setPrenom(moniteur.getPrenom());
                dto.setEmail(moniteur.getEmail());
                dto.setTelephone(moniteur.getTelephone());
                // Add more fields if needed
                return dto;
            })
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Moniteur>> getAvailableMoniteurs() {
        return ResponseEntity.ok(moniteurService.getAvailableMoniteurs());
    }

    @GetMapping("/specialite/{specialite}")
    public ResponseEntity<List<Moniteur>> getMoniteursBySpecialite(@PathVariable String specialite) {
        return ResponseEntity.ok(moniteurService.getMoniteursBySpecialite(specialite));
    }

    @Operation(summary = "Create a new moniteur", description = "Creates a new driving instructor")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully created the moniteur",
                    content = @Content(schema = @Schema(implementation = Moniteur.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    @PostMapping
    public ResponseEntity<Moniteur> createMoniteur(
            @Valid @RequestBody CreateMoniteurRequest request) {
        return ResponseEntity.ok(moniteurService.createMoniteur(request));
    }

    @Operation(summary = "Update a moniteur", description = "Updates an existing moniteur's information")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully updated the moniteur"),
        @ApiResponse(responseCode = "404", description = "Moniteur not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Moniteur> updateMoniteur(
            @Parameter(description = "ID of the moniteur to update") @PathVariable Long id,
            @Valid @RequestBody CreateMoniteurRequest request) {
        return ResponseEntity.ok(moniteurService.updateMoniteur(id, request));
    }

    @Operation(summary = "Delete a moniteur", description = "Deletes a moniteur by their ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully deleted the moniteur"),
        @ApiResponse(responseCode = "404", description = "Moniteur not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMoniteur(
            @Parameter(description = "ID of the moniteur to delete") @PathVariable Long id) {
        moniteurService.deleteMoniteur(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update moniteur availability", description = "Updates a moniteur's availability status")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully updated availability"),
        @ApiResponse(responseCode = "404", description = "Moniteur not found")
    })
    @PutMapping("/{id}/disponibilite")
    public ResponseEntity<Moniteur> updateDisponibilite(
            @Parameter(description = "ID of the moniteur") @PathVariable Long id,
            @Parameter(description = "New availability status") @RequestParam boolean disponible) {
        return ResponseEntity.ok(moniteurService.updateDisponibilite(id, disponible));
    }
} 