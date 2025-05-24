package com.perm.dto.mapper;

import com.perm.dto.response.UserResponse;
import com.perm.models.user.Admin;
import com.perm.models.user.Client;
import com.perm.models.user.Moniteur;
import com.perm.models.user.Utilisateur;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toUserResponse(Utilisateur utilisateur) {
        UserResponse response = new UserResponse();
        response.setId(utilisateur.getId());
        response.setNom(utilisateur.getNom());
        response.setPrenom(utilisateur.getPrenom());
        response.setEmail(utilisateur.getEmail());
        response.setTelephone(utilisateur.getTelephone());
        response.setDateCreation(utilisateur.getDateCreation());

        // Déterminer le type et ajouter les propriétés spécifiques
        if (utilisateur instanceof Admin) {
            response.setType("ADMIN");
            response.setAutoEcoleId(((Admin) utilisateur).getAutoEcoleId());
        } else if (utilisateur instanceof Client) {
            Client client = (Client) utilisateur;
            response.setType("CLIENT");
            response.setProgression(client.getProgression());
            response.setDateInscription(client.getDateInscription());
            response.setStatut(client.getStatut());
        } else if (utilisateur instanceof Moniteur) {
            Moniteur moniteur = (Moniteur) utilisateur;
            response.setType("MONITEUR");
            response.setSpecialite(moniteur.getSpecialite());
            response.setExperience(moniteur.getExperience());
            response.setDisponibilite(moniteur.getDisponibilite());
        }

        return response;
    }
}
