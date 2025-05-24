package com.perm.controllers;

import com.perm.dto.CoursDTO;
import com.perm.dto.request.CoursRequestDTO;
import com.perm.models.user.Client;
import com.perm.security.CurrentUser;
import com.perm.services.interfaces.CoursService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cours")
public class CoursController {

    private final CoursService coursService;

    @Autowired
    public CoursController(CoursService coursService) {
        this.coursService = coursService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CoursDTO> creerCours(@Valid @RequestBody CoursRequestDTO coursDTO) {
        CoursDTO createdCours = coursService.createCours(coursDTO);
        return new ResponseEntity<>(createdCours, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoursDTO> getCours(@PathVariable Long id, @CurrentUser Client client) {
        CoursDTO cours = coursService.getCours(id, client);
        return ResponseEntity.ok(cours);
    }

    @GetMapping
    public ResponseEntity<List<CoursDTO>> getAllCours(@CurrentUser Client client) {
        List<CoursDTO> cours = coursService.getAllCours(client);
        return ResponseEntity.ok(cours);
    }

    @GetMapping("/auto-ecole/{autoEcoleId}")
    public ResponseEntity<List<CoursDTO>> getCoursByAutoEcole(
            @PathVariable Long autoEcoleId,
            @CurrentUser Client client) {
        List<CoursDTO> cours = coursService.getCoursByAutoEcole(autoEcoleId, client);
        return ResponseEntity.ok(cours);
    }

    @GetMapping("/actifs")
    public ResponseEntity<List<CoursDTO>> getCoursActifs(@CurrentUser Client client) {
        List<CoursDTO> cours = coursService.getCoursActifs(client);
        return ResponseEntity.ok(cours);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CoursDTO>> searchCours(
            @RequestParam String motCle,
            @CurrentUser Client client) {
        List<CoursDTO> cours = coursService.rechercherCours(motCle, client);
        return ResponseEntity.ok(cours);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CoursDTO> updateCours(
            @PathVariable Long id,
            @Valid @RequestBody CoursRequestDTO coursDTO) {
        CoursDTO updatedCours = coursService.updateCours(id, coursDTO);
        return ResponseEntity.ok(updatedCours);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCours(@PathVariable Long id) {
        coursService.deleteCours(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/activer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> activerCours(@PathVariable Long id) {
        coursService.activerCours(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/desactiver")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desactiverCours(@PathVariable Long id) {
        coursService.desactiverCours(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/contenu")
    public ResponseEntity<String> consulterContenu(
            @PathVariable Long id,
            @CurrentUser Client client) {
        String contenu = coursService.consulterContenu(id, client);
        return ResponseEntity.ok(contenu);
    }

    @PutMapping("/{id}/contenu")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> modifierContenu(
            @PathVariable Long id,
            @RequestBody String nouveauContenu) {
        coursService.modifierContenu(id, nouveauContenu);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/access")
    public ResponseEntity<Boolean> verifierAccesCours(
            @PathVariable Long id,
            @CurrentUser Client client) {
        boolean hasAccess = coursService.verifierAccesCours(id, client);
        return ResponseEntity.ok(hasAccess);
    }
}
