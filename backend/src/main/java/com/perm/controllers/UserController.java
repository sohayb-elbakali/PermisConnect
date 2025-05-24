package com.perm.controllers;

import com.perm.dto.request.UserRequest;
import com.perm.dto.response.UserResponse;
import com.perm.models.user.Admin;
import com.perm.models.user.Client;
import com.perm.models.user.Moniteur;
import com.perm.models.user.Utilisateur;
import com.perm.services.interfaces.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ----- Méthodes génériques pour tous les utilisateurs -----

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.updateUser(id, userRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // ----- Méthodes spécifiques pour les administrateurs -----

    @GetMapping("/admins")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return ResponseEntity.ok(userService.getAllAdmins());
    }

    @PostMapping("/admins")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Admin> createAdmin(@Valid @RequestBody UserRequest adminRequest) {
        Admin admin = userService.createAdmin(adminRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(admin);
    }

    // ----- Méthodes spécifiques pour les clients -----

    @GetMapping("/clients")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Client>> getAllClients() {
        return ResponseEntity.ok(userService.getAllClients());
    }

    @PostMapping("/clients")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Client> createClient(@Valid @RequestBody UserRequest clientRequest) {
        Client client = userService.createClient(clientRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(client);
    }

    @GetMapping("/clients/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#id) or hasRole('MONITEUR')")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getClientById(id));
    }

    @GetMapping("/clients/{id}/progression")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#id) or hasRole('MONITEUR')")
    public ResponseEntity<Double> getClientProgression(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getClientProgression(id));
    }

    // ----- Méthodes spécifiques pour les moniteurs -----

    @GetMapping("/moniteurs")
    public ResponseEntity<List<Moniteur>> getAllMoniteurs() {
        return ResponseEntity.ok(userService.getAllMoniteurs());
    }

    @PostMapping("/moniteurs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Moniteur> createMoniteur(@Valid @RequestBody UserRequest moniteurRequest) {
        Moniteur moniteur = userService.createMoniteur(moniteurRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(moniteur);
    }

    @GetMapping("/moniteurs/{id}")
    public ResponseEntity<Moniteur> getMoniteurById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getMoniteurById(id));
    }

    @PutMapping("/moniteurs/{id}/disponibilite")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<Moniteur> updateMoniteurDisponibilite(
            @PathVariable Long id,
            @RequestBody String disponibilite) {
        return ResponseEntity.ok(userService.updateMoniteurDisponibilite(id, disponibilite));
    }

    // ----- Recherche et filtrage -----

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<? extends Utilisateur>> searchUsers(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String prenom,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String type) {
        return ResponseEntity.ok(userService.searchUsers(nom, prenom, email, type));
    }
}
