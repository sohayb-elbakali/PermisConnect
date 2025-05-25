package com.autoecole.controllers;

import com.autoecole.models.Moniteur;
import com.autoecole.services.MoniteurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/moniteurs")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
public class MoniteurController {
    private final MoniteurService moniteurService;

    @GetMapping
    public ResponseEntity<List<Moniteur>> getAllMoniteurs() {
        return ResponseEntity.ok(moniteurService.getAllMoniteurs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Moniteur> getMoniteurById(@PathVariable Long id) {
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
    public ResponseEntity<List<Moniteur>> getMoniteursByAutoEcole(@PathVariable Long autoEcoleId) {
        return ResponseEntity.ok(moniteurService.getMoniteursByAutoEcole(autoEcoleId));
    }

    @GetMapping("/available")
    public ResponseEntity<List<Moniteur>> getAvailableMoniteurs() {
        return ResponseEntity.ok(moniteurService.getAvailableMoniteurs());
    }

    @GetMapping("/specialite/{specialite}")
    public ResponseEntity<List<Moniteur>> getMoniteursBySpecialite(@PathVariable String specialite) {
        return ResponseEntity.ok(moniteurService.getMoniteursBySpecialite(specialite));
    }

    @PostMapping
    public ResponseEntity<Moniteur> createMoniteur(@RequestBody Moniteur moniteur) {
        return ResponseEntity.ok(moniteurService.createMoniteur(moniteur));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Moniteur> updateMoniteur(@PathVariable Long id, @RequestBody Moniteur moniteur) {
        return ResponseEntity.ok(moniteurService.updateMoniteur(id, moniteur));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMoniteur(@PathVariable Long id) {
        moniteurService.deleteMoniteur(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/disponibilite")
    public ResponseEntity<Moniteur> updateDisponibilite(
            @PathVariable Long id,
            @RequestParam boolean disponible) {
        return ResponseEntity.ok(moniteurService.updateDisponibilite(id, disponible));
    }
} 