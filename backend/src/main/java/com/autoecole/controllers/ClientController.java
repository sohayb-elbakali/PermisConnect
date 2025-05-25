package com.autoecole.controllers;

import com.autoecole.dto.request.ClientRequest;
import com.autoecole.dto.response.ClientResponse;
import com.autoecole.models.Client;
import com.autoecole.services.ClientService;
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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
@Tag(name = "Clients", description = "API pour la gestion des clients")
public class ClientController {
    private final ClientService clientService;

    @Operation(summary = "Créer un nouveau client")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Client créé avec succès",
            content = @Content(schema = @Schema(implementation = ClientResponse.class))),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "409", description = "Email ou téléphone déjà utilisé")
    })
    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@Valid @RequestBody ClientRequest request) {
        try {
            Client client = clientService.createClient(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ClientResponse.fromEntity(client));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Operation(summary = "Obtenir tous les clients")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des clients récupérée avec succès",
            content = @Content(schema = @Schema(implementation = ClientResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAllClients() {
        List<Client> clients = clientService.getAllClients();
        List<ClientResponse> responses = clients.stream()
                .map(ClientResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Obtenir un client par son ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Client récupéré avec succès",
            content = @Content(schema = @Schema(implementation = ClientResponse.class))),
        @ApiResponse(responseCode = "404", description = "Client non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClientById(
            @Parameter(description = "ID du client", required = true)
            @PathVariable Long id) {
        try {
            Client client = clientService.getClientById(id);
            return ResponseEntity.ok(ClientResponse.fromEntity(client));
        } catch (jakarta.persistence.EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Mettre à jour un client")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Client mis à jour avec succès",
            content = @Content(schema = @Schema(implementation = ClientResponse.class))),
        @ApiResponse(responseCode = "404", description = "Client non trouvé"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "409", description = "Email ou téléphone déjà utilisé")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(
            @Parameter(description = "ID du client", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ClientRequest request) {
        try {
            Client client = clientService.updateClient(id, request);
            return ResponseEntity.ok(ClientResponse.fromEntity(client));
        } catch (jakarta.persistence.EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Operation(summary = "Supprimer un client")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Client supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Client non trouvé")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(
            @Parameter(description = "ID du client", required = true)
            @PathVariable Long id) {
        try {
            clientService.deleteClient(id);
            return ResponseEntity.noContent().build();
        } catch (jakarta.persistence.EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Rechercher des clients")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recherche effectuée avec succès",
            content = @Content(schema = @Schema(implementation = ClientResponse.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<List<ClientResponse>> searchClients(
            @Parameter(description = "Terme de recherche")
            @RequestParam(required = false) String query) {
        List<Client> clients = clientService.searchClients(query);
        List<ClientResponse> responses = clients.stream()
                .map(ClientResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
} 