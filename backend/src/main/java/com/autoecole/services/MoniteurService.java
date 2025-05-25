package com.autoecole.services;

import com.autoecole.models.Moniteur;
import com.autoecole.repositories.MoniteurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MoniteurService {
    private final MoniteurRepository moniteurRepository;

    public List<Moniteur> getAllMoniteurs() {
        return moniteurRepository.findAll();
    }

    public Optional<Moniteur> getMoniteurById(Long id) {
        return moniteurRepository.findById(id);
    }

    public Optional<Moniteur> getMoniteurByEmail(String email) {
        return moniteurRepository.findByEmail(email);
    }

    public List<Moniteur> getMoniteursByAutoEcole(Long autoEcoleId) {
        return moniteurRepository.findByAutoEcoleId(autoEcoleId);
    }

    public List<Moniteur> getAvailableMoniteurs() {
        return moniteurRepository.findByDisponibleTrue();
    }

    public List<Moniteur> getMoniteursBySpecialite(String specialite) {
        return moniteurRepository.findBySpecialite(specialite);
    }

    @Transactional
    public Moniteur createMoniteur(Moniteur moniteur) {
        if (moniteurRepository.existsByEmail(moniteur.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        return moniteurRepository.save(moniteur);
    }

    @Transactional
    public Moniteur updateMoniteur(Long id, Moniteur moniteurDetails) {
        Moniteur moniteur = moniteurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Moniteur not found"));
        
        moniteur.setNom(moniteurDetails.getNom());
        moniteur.setPrenom(moniteurDetails.getPrenom());
        moniteur.setEmail(moniteurDetails.getEmail());
        moniteur.setSpecialite(moniteurDetails.getSpecialite());
        moniteur.setExperienceAnnees(moniteurDetails.getExperienceAnnees());
        moniteur.setNumeroAgrement(moniteurDetails.getNumeroAgrement());
        moniteur.setDisponible(moniteurDetails.getDisponible());
        moniteur.setAutoEcole(moniteurDetails.getAutoEcole());
        
        return moniteurRepository.save(moniteur);
    }

    @Transactional
    public void deleteMoniteur(Long id) {
        if (!moniteurRepository.existsById(id)) {
            throw new RuntimeException("Moniteur not found");
        }
        moniteurRepository.deleteById(id);
    }

    @Transactional
    public Moniteur updateDisponibilite(Long id, boolean disponible) {
        Moniteur moniteur = moniteurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Moniteur not found"));
        moniteur.setDisponible(disponible);
        return moniteurRepository.save(moniteur);
    }
} 