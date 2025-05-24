package com.perm.controllers;

import com.perm.dto.request.AutoEcoleRequest;
import com.perm.dto.response.AutoEcoleResponse;
import com.perm.dto.response.StatistiquesAutoEcole;
import com.perm.services.interfaces.AutoEcoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/autoecoles")
public class AutoEcoleController {

    private final AutoEcoleService autoEcoleService;

    @Autowired
    public AutoEcoleController(AutoEcoleService autoEcoleService) {
        this.autoEcoleService = autoEcoleService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AutoEcoleResponse> creerAutoEcole(@Valid @RequestBody AutoEcoleRequest autoEcoleRequest) {
        AutoEcoleResponse createdAutoEcole = autoEcoleService.creerAutoEcole(autoEcoleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAutoEcole);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutoEcoleResponse> getAutoEcoleById(@PathVariable Long id) {
        return ResponseEntity.ok(autoEcoleService.getAutoEcoleById(id));
    }

    @GetMapping("/agrement/{numeroAgrement}")
    public ResponseEntity<AutoEcoleResponse> getAutoEcoleByNumeroAgrement(@PathVariable String numeroAgrement) {
        return ResponseEntity.ok(autoEcoleService.getAutoEcoleByNumeroAgrement(numeroAgrement));
    }

    @GetMapping
    public ResponseEntity<List<AutoEcoleResponse>> getAllAutoEcoles() {
        return ResponseEntity.ok(autoEcoleService.getAllAutoEcoles());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AutoEcoleResponse> updateAutoEcole(
            @PathVariable Long id,
            @Valid @RequestBody AutoEcoleRequest autoEcoleRequest) {
        return ResponseEntity.ok(autoEcoleService.updateAutoEcole(id, autoEcoleRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAutoEcole(@PathVariable Long id) {
        autoEcoleService.deleteAutoEcole(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/nom")
    public ResponseEntity<List<AutoEcoleResponse>> rechercherAutoEcoleParNom(@RequestParam String nom) {
        return ResponseEntity.ok(autoEcoleService.rechercherAutoEcoleParNom(nom));
    }

    @GetMapping("/search/ville")
    public ResponseEntity<List<AutoEcoleResponse>> rechercherAutoEcoleParVille(@RequestParam String ville) {
        return ResponseEntity.ok(autoEcoleService.rechercherAutoEcoleParVille(ville));
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsByNumeroAgrement(@RequestParam String numeroAgrement) {
        return ResponseEntity.ok(autoEcoleService.existsByNumeroAgrement(numeroAgrement));
    }

    @PutMapping("/{autoEcoleId}/admin/{adminId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> associerAdmin(
            @PathVariable Long autoEcoleId,
            @PathVariable Long adminId) {
        autoEcoleService.associerAdmin(autoEcoleId, adminId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{autoEcoleId}/clients/{clientId}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> ajouterClient(
            @PathVariable Long autoEcoleId,
            @PathVariable Long clientId) {
        autoEcoleService.ajouterClient(autoEcoleId, clientId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{autoEcoleId}/moniteurs/{moniteurId}")
    @PreAuthorize("hasRole('MONITEUR')")
    public ResponseEntity<Void> ajouterMoniteur(
            @PathVariable Long autoEcoleId,
            @PathVariable Long moniteurId) {
        autoEcoleService.ajouterMoniteur(autoEcoleId, moniteurId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/statistiques")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StatistiquesAutoEcole> obtenirStatistiques(@PathVariable Long id) {
        return ResponseEntity.ok(autoEcoleService.obtenirStatistiques(id));
    }

    @GetMapping("/statistiques/generales")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> obtenirStatistiquesGenerales() {
        return ResponseEntity.ok(autoEcoleService.obtenirStatistiquesGenerales());
    }
}