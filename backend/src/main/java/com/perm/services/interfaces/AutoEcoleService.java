package com.perm.services.interfaces;

import com.perm.dto.request.AutoEcoleRequest;
import com.perm.dto.response.AutoEcoleResponse;
import com.perm.dto.response.StatistiquesAutoEcole;

import java.util.List;
import java.util.Map;

public interface AutoEcoleService {

    // Opérations CRUD de base
    AutoEcoleResponse creerAutoEcole(AutoEcoleRequest autoEcoleRequest);

    AutoEcoleResponse getAutoEcoleById(Long id);

    AutoEcoleResponse getAutoEcoleByNumeroAgrement(String numeroAgrement);

    List<AutoEcoleResponse> getAllAutoEcoles();

    AutoEcoleResponse updateAutoEcole(Long id, AutoEcoleRequest autoEcoleRequest);

    void deleteAutoEcole(Long id);

    // Méthodes spécifiques
    List<AutoEcoleResponse> rechercherAutoEcoleParNom(String nom);

    List<AutoEcoleResponse> rechercherAutoEcoleParVille(String ville);

    boolean existsByNumeroAgrement(String numeroAgrement);

    // Gestion des utilisateurs liés à l'auto-école
    void associerAdmin(Long autoEcoleId, Long adminId);

    void ajouterClient(Long autoEcoleId, Long clientId);

    void ajouterMoniteur(Long autoEcoleId, Long moniteurId);

    // Statistiques
    StatistiquesAutoEcole obtenirStatistiques(Long autoEcoleId);

    Map<String, Object> obtenirStatistiquesGenerales();
}
