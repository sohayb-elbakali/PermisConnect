package com.perm.services.interfaces;

import com.perm.dto.request.UserRequest;
import com.perm.dto.response.UserResponse;
import com.perm.models.user.Admin;
import com.perm.models.user.Client;
import com.perm.models.user.Moniteur;
import com.perm.models.user.Utilisateur;

import java.util.List;

public interface UserService {

    // Méthodes génériques
    UserResponse getUserById(Long id);
    UserResponse updateUser(Long id, UserRequest userRequest);
    void deleteUser(Long id);

    // Méthodes pour les admins
    List<Admin> getAllAdmins();
    Admin createAdmin(UserRequest adminRequest);

    // Méthodes pour les clients
    List<Client> getAllClients();
    Client createClient(UserRequest clientRequest);
    Client getClientById(Long id);
    Double getClientProgression(Long id);

    // Méthodes pour les moniteurs
    List<Moniteur> getAllMoniteurs();
    Moniteur createMoniteur(UserRequest moniteurRequest);
    Moniteur getMoniteurById(Long id);
    Moniteur updateMoniteurDisponibilite(Long id, String disponibilite);

    // Recherche et filtrage
    List<? extends Utilisateur> searchUsers(String nom, String prenom, String email, String type);
}
