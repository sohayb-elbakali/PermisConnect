package com.autoecole.services;

import com.autoecole.dto.request.AutoEcoleRequest;
import com.autoecole.models.AutoEcole;
import com.autoecole.repositories.AutoEcoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AutoEcoleService {
    private final AutoEcoleRepository autoEcoleRepository;

    @Transactional
    public AutoEcole createAutoEcole(AutoEcoleRequest request) {
        if (autoEcoleRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Une auto-école avec cet email existe déjà");
        }
        if (autoEcoleRepository.existsByTelephone(request.getTelephone())) {
            throw new IllegalArgumentException("Une auto-école avec ce numéro de téléphone existe déjà");
        }
        if (autoEcoleRepository.existsBySiret(request.getSiret())) {
            throw new IllegalArgumentException("Une auto-école avec ce numéro SIRET existe déjà");
        }

        AutoEcole autoEcole = new AutoEcole();
        updateAutoEcoleFromRequest(autoEcole, request);
        return autoEcoleRepository.save(autoEcole);
    }

    public List<AutoEcole> getAllAutoEcoles() {
        return autoEcoleRepository.findAll();
    }

    public AutoEcole getAutoEcoleById(Long id) {
        return autoEcoleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Auto-école non trouvée avec l'ID: " + id));
    }

    @Transactional
    public AutoEcole updateAutoEcole(Long id, AutoEcoleRequest request) {
        AutoEcole autoEcole = getAutoEcoleById(id);
        
        // Vérifier si l'email est déjà utilisé par une autre auto-école
        if (!autoEcole.getEmail().equals(request.getEmail()) && 
            autoEcoleRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Une auto-école avec cet email existe déjà");
        }
        
        // Vérifier si le téléphone est déjà utilisé par une autre auto-école
        if (!autoEcole.getTelephone().equals(request.getTelephone()) && 
            autoEcoleRepository.existsByTelephone(request.getTelephone())) {
            throw new IllegalArgumentException("Une auto-école avec ce numéro de téléphone existe déjà");
        }

        // Vérifier si le SIRET est déjà utilisé par une autre auto-école
        if (!autoEcole.getSiret().equals(request.getSiret()) && 
            autoEcoleRepository.existsBySiret(request.getSiret())) {
            throw new IllegalArgumentException("Une auto-école avec ce numéro SIRET existe déjà");
        }

        updateAutoEcoleFromRequest(autoEcole, request);
        return autoEcoleRepository.save(autoEcole);
    }

    @Transactional
    public void deleteAutoEcole(Long id) {
        if (!autoEcoleRepository.existsById(id)) {
            throw new EntityNotFoundException("Auto-école non trouvée avec l'ID: " + id);
        }
        autoEcoleRepository.deleteById(id);
    }

    public List<AutoEcole> searchAutoEcoles(String query) {
        if (query == null || query.trim().isEmpty()) {
            return autoEcoleRepository.findAll();
        }
        
        String searchTerm = query.trim();
        List<AutoEcole> results = autoEcoleRepository.findByNomContainingIgnoreCase(searchTerm);
        results.addAll(autoEcoleRepository.findByEmailContainingIgnoreCase(searchTerm));
        results.addAll(autoEcoleRepository.findByVilleContainingIgnoreCase(searchTerm));
        
        return results.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public List<AutoEcole> findByVilleAndCodePostal(String ville, String codePostal) {
        return autoEcoleRepository.findByVilleAndCodePostal(ville, codePostal);
    }

    private void updateAutoEcoleFromRequest(AutoEcole autoEcole, AutoEcoleRequest request) {
        autoEcole.setNom(request.getNom());
        autoEcole.setEmail(request.getEmail());
        autoEcole.setTelephone(request.getTelephone());
        autoEcole.setAdresse(request.getAdresse());
        autoEcole.setSiret(request.getSiret());
        autoEcole.setCodePostal(request.getCodePostal());
        autoEcole.setVille(request.getVille());
        autoEcole.setSiteWeb(request.getSiteWeb());
        autoEcole.setDescription(request.getDescription());
        autoEcole.setHoraires(request.getHoraires());
    }
} 