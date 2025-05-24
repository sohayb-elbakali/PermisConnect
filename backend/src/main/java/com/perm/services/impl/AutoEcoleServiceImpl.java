package com.perm.services.impl;

import com.perm.dto.mapper.AutoEcoleMapper;
import com.perm.dto.request.AutoEcoleRequest;
import com.perm.dto.response.AutoEcoleResponse;
import com.perm.dto.response.StatistiquesAutoEcole;
import com.perm.exceptions.BusinessException;
import com.perm.exceptions.ResourceNotFoundException;
import com.perm.models.AutoEcole;
import com.perm.models.user.Admin;
import com.perm.models.user.Client;
import com.perm.models.user.Moniteur;
import com.perm.repositories.AdminRepository;
import com.perm.repositories.AutoEcoleRepository;
import com.perm.repositories.ClientRepository;
import com.perm.repositories.MoniteurRepository;
import com.perm.services.interfaces.AutoEcoleService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AutoEcoleServiceImpl implements AutoEcoleService {

    private final AutoEcoleRepository autoEcoleRepository;
    private final AdminRepository adminRepository;
    private final ClientRepository clientRepository;
    private final MoniteurRepository moniteurRepository;
    private final AutoEcoleMapper autoEcoleMapper;

    @Autowired
    public AutoEcoleServiceImpl(
            AutoEcoleRepository autoEcoleRepository,
            AdminRepository adminRepository,
            ClientRepository clientRepository,
            MoniteurRepository moniteurRepository,
            AutoEcoleMapper autoEcoleMapper) {
        this.autoEcoleRepository = autoEcoleRepository;
        this.adminRepository = adminRepository;
        this.clientRepository = clientRepository;
        this.moniteurRepository = moniteurRepository;
        this.autoEcoleMapper = autoEcoleMapper;
    }

    @Override
    @Transactional
    public AutoEcoleResponse creerAutoEcole(AutoEcoleRequest autoEcoleRequest) {
        // Vérifier si le numéro d'agrément existe déjà
        if (autoEcoleRepository.existsByNumeroAgrement(autoEcoleRequest.getNumeroAgrement())) {
            throw new BusinessException("ERR_AUTOECOLE_001", "Une auto-école avec ce numéro d'agrément existe déjà");
        }

        // Vérifier si l'email existe déjà
        if (autoEcoleRepository.existsByEmail(autoEcoleRequest.getEmail())) {
            throw new BusinessException("Une auto-école avec cet email existe déjà");
        }

        // Créer une nouvelle auto-école
        AutoEcole autoEcole = new AutoEcole();
        autoEcole.setNom(autoEcoleRequest.getNom());
        autoEcole.setAdresse(autoEcoleRequest.getAdresse());
        autoEcole.setTelephone(autoEcoleRequest.getTelephone());
        autoEcole.setEmail(autoEcoleRequest.getEmail());
        autoEcole.setNumeroAgrement(autoEcoleRequest.getNumeroAgrement());
        autoEcole.setDateCreation(LocalDateTime.now());

        AutoEcole savedAutoEcole = autoEcoleRepository.save(autoEcole);
        return autoEcoleMapper.toAutoEcoleResponse(savedAutoEcole);
    }

    @Override
    public AutoEcoleResponse getAutoEcoleById(Long id) {
        AutoEcole autoEcole = findAutoEcoleById(id);
        return autoEcoleMapper.toAutoEcoleResponse(autoEcole);
    }

    @Override
    public AutoEcoleResponse getAutoEcoleByNumeroAgrement(String numeroAgrement) {
        AutoEcole autoEcole = autoEcoleRepository.findByNumeroAgrement(numeroAgrement)
                .orElseThrow(() -> new ResourceNotFoundException("Auto-école non trouvée avec le numéro d'agrément : " + numeroAgrement));
        return autoEcoleMapper.toAutoEcoleResponse(autoEcole);
    }

    @Override
    public List<AutoEcoleResponse> getAllAutoEcoles() {
        return autoEcoleRepository.findAll()
                .stream()
                .map(autoEcoleMapper::toAutoEcoleResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AutoEcoleResponse updateAutoEcole(Long id, AutoEcoleRequest autoEcoleRequest) {
        AutoEcole existingAutoEcole = findAutoEcoleById(id);

        // Vérifier si le numéro d'agrément est modifié et s'il n'appartient pas déjà à une autre auto-école
        if (!existingAutoEcole.getNumeroAgrement().equals(autoEcoleRequest.getNumeroAgrement()) &&
                autoEcoleRepository.existsByNumeroAgrement(autoEcoleRequest.getNumeroAgrement())) {
            throw new BusinessException("Une autre auto-école avec ce numéro d'agrément existe déjà");
        }

        // Vérifier si l'email est modifié et s'il n'appartient pas déjà à une autre auto-école
        if (!existingAutoEcole.getEmail().equals(autoEcoleRequest.getEmail()) &&
                autoEcoleRepository.existsByEmail(autoEcoleRequest.getEmail())) {
            throw new BusinessException("Une autre auto-école avec cet email existe déjà");
        }

        // Mettre à jour les champs
        existingAutoEcole.setNom(autoEcoleRequest.getNom());
        existingAutoEcole.setAdresse(autoEcoleRequest.getAdresse());
        existingAutoEcole.setTelephone(autoEcoleRequest.getTelephone());
        existingAutoEcole.setEmail(autoEcoleRequest.getEmail());
        existingAutoEcole.setNumeroAgrement(autoEcoleRequest.getNumeroAgrement());

        AutoEcole updatedAutoEcole = autoEcoleRepository.save(existingAutoEcole);
        return autoEcoleMapper.toAutoEcoleResponse(updatedAutoEcole);
    }

    @Override
    @Transactional
    public void deleteAutoEcole(Long id) {
        if (!autoEcoleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Auto-école non trouvée avec l'id : " + id);
        }
        autoEcoleRepository.deleteById(id);
    }

    @Override
    public List<AutoEcoleResponse> rechercherAutoEcoleParNom(String nom) {
        return autoEcoleRepository.findByNomContainingIgnoreCase(nom)
                .stream()
                .map(autoEcoleMapper::toAutoEcoleResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AutoEcoleResponse> rechercherAutoEcoleParVille(String ville) {
        return autoEcoleRepository.findByVille(ville)
                .stream()
                .map(autoEcoleMapper::toAutoEcoleResponse)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByNumeroAgrement(String numeroAgrement) {
        return autoEcoleRepository.existsByNumeroAgrement(numeroAgrement);
    }

    @Override
    @Transactional
    public void associerAdmin(Long autoEcoleId, Long adminId) {
        AutoEcole autoEcole = findAutoEcoleById(autoEcoleId);
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin non trouvé avec l'id : " + adminId));

        if (admin.getAutoEcole() != null) {
            throw new BusinessException("Cet administrateur est déjà associé à une auto-école");
        }

        admin.setAutoEcole(autoEcole);
        autoEcole.setAdmin(admin);

        adminRepository.save(admin);
        autoEcoleRepository.save(autoEcole);
    }

    @Override
    @Transactional
    public void ajouterClient(Long autoEcoleId, Long clientId) {
        AutoEcole autoEcole = findAutoEcoleById(autoEcoleId);
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec l'id : " + clientId));

        client.setAutoEcole(autoEcole);
        clientRepository.save(client);
    }

    @Override
    @Transactional
    public void ajouterMoniteur(Long autoEcoleId, Long moniteurId) {
        AutoEcole autoEcole = findAutoEcoleById(autoEcoleId);
        Moniteur moniteur = moniteurRepository.findById(moniteurId)
                .orElseThrow(() -> new ResourceNotFoundException("Moniteur non trouvé avec l'id : " + moniteurId));

        moniteur.setAutoEcole(autoEcole);
        moniteurRepository.save(moniteur);
    }

    @Override
    public StatistiquesAutoEcole obtenirStatistiques(Long autoEcoleId) {
        AutoEcole autoEcole = findAutoEcoleById(autoEcoleId);

        StatistiquesAutoEcole statistiques = new StatistiquesAutoEcole();
        statistiques.setNombreClients(autoEcoleRepository.countClientsByAutoEcoleId(autoEcoleId));
        statistiques.setNombreMoniteurs(autoEcoleRepository.countMoniteursByAutoEcoleId(autoEcoleId));

        // Vous pouvez ajouter d'autres statistiques ici en fonction de vos besoins
        // Par exemple, le nombre de cours, de tests blancs, etc.

        return statistiques;
    }

    @Override
    public Map<String, Object> obtenirStatistiquesGenerales() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("nombreAutoEcoles", autoEcoleRepository.count());

        // Ajouter d'autres statistiques générales selon les besoins

        return stats;
    }

    // Méthode d'aide pour trouver une auto-école par ID
    private AutoEcole findAutoEcoleById(Long id) {
        return autoEcoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auto-école non trouvée avec l'id : " + id));
    }
}
