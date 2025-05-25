package com.autoecole.dto.response;

import com.autoecole.models.Client;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Données de réponse pour un client")
public class ClientResponse {
    @Schema(description = "ID du client", example = "1")
    private Long id;

    @Schema(description = "Nom du client", example = "Doe")
    private String nom;

    @Schema(description = "Prénom du client", example = "John")
    private String prenom;

    @Schema(description = "Email du client", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Numéro de téléphone du client", example = "+33612345678")
    private String telephone;

    @Schema(description = "Adresse du client", example = "123 rue de Paris")
    private String adresse;

    @Schema(description = "Date de naissance du client", example = "1990-01-01")
    private String dateNaissance;

    @Schema(description = "Numéro de permis du client", example = "123456789")
    private String numeroPermis;

    @Schema(description = "Type de permis du client", example = "B")
    private String typePermis;

    public static ClientResponse fromEntity(Client client) {
        ClientResponse response = new ClientResponse();
        response.setId(client.getId());
        response.setNom(client.getUser().getNom());
        response.setPrenom(client.getUser().getPrenom());
        response.setEmail(client.getUser().getEmail());
        response.setTelephone(client.getUser().getTelephone());
        response.setAdresse(client.getUser().getAdresse());
        response.setDateNaissance(client.getDateNaissance());
        response.setNumeroPermis(client.getNumeroPermis());
        response.setTypePermis(client.getTypePermis());
        return response;
    }
} 