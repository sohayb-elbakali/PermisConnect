package com.autoecole.services;

import com.autoecole.dto.CreateMoniteurRequest;
import com.autoecole.models.AutoEcole;
import com.autoecole.models.Moniteur;
import com.autoecole.repositories.AutoEcoleRepository;
import com.autoecole.repositories.MoniteurRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class MoniteurService {
    private final MoniteurRepository moniteurRepository;
    private final AutoEcoleRepository autoEcoleRepository;

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
    public Moniteur createMoniteur(CreateMoniteurRequest request) {
        if (moniteurRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Email already exists");
        }

        Moniteur moniteur = new Moniteur();
        moniteur.setNom(request.getNom());
        moniteur.setPrenom(request.getPrenom());
        moniteur.setEmail(request.getEmail());
        moniteur.setPassword(Base64.getEncoder().encodeToString(request.getPassword().getBytes()));
        moniteur.setTelephone(request.getTelephone());
        moniteur.setAdresse(request.getAdresse());
        moniteur.setSpecialite(request.getSpecialite());
        moniteur.setExperienceAnnees(request.getExperienceAnnees());
        moniteur.setNumeroAgrement(request.getNumeroAgrement());
        moniteur.setDisponible(true);

        if (request.getAutoEcoleId() != null) {
            AutoEcole autoEcole = autoEcoleRepository.findById(request.getAutoEcoleId())
                    .orElseThrow(() -> new EntityNotFoundException("Auto-école not found with ID: " + request.getAutoEcoleId()));
            moniteur.setAutoEcole(autoEcole);
        }

        try {
            return moniteurRepository.save(moniteur);
        } catch (Exception e) {
            throw new RuntimeException("Error creating moniteur: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Moniteur updateMoniteur(Long id, CreateMoniteurRequest request) {
        Moniteur moniteur = moniteurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Moniteur not found with ID: " + id));
        
        moniteur.setNom(request.getNom());
        moniteur.setPrenom(request.getPrenom());
        moniteur.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            moniteur.setPassword(Base64.getEncoder().encodeToString(request.getPassword().getBytes()));
        }
        moniteur.setTelephone(request.getTelephone());
        moniteur.setAdresse(request.getAdresse());
        moniteur.setSpecialite(request.getSpecialite());
        moniteur.setExperienceAnnees(request.getExperienceAnnees());
        moniteur.setNumeroAgrement(request.getNumeroAgrement());

        if (request.getAutoEcoleId() != null) {
            AutoEcole autoEcole = autoEcoleRepository.findById(request.getAutoEcoleId())
                    .orElseThrow(() -> new EntityNotFoundException("Auto-école not found with ID: " + request.getAutoEcoleId()));
            moniteur.setAutoEcole(autoEcole);
        }
        
        try {
            return moniteurRepository.save(moniteur);
        } catch (Exception e) {
            throw new RuntimeException("Error updating moniteur: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void deleteMoniteur(Long id) {
        if (!moniteurRepository.existsById(id)) {
            throw new EntityNotFoundException("Moniteur not found with ID: " + id);
        }
        try {
            moniteurRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting moniteur: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Moniteur updateDisponibilite(Long id, boolean disponible) {
        Moniteur moniteur = moniteurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Moniteur not found with ID: " + id));
        moniteur.setDisponible(disponible);
        try {
            return moniteurRepository.save(moniteur);
        } catch (Exception e) {
            throw new RuntimeException("Error updating moniteur availability: " + e.getMessage(), e);
        }
    }
} 