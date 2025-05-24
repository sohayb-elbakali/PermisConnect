package com.perm.services.impl;

import com.perm.dto.CoursDTO;
import com.perm.dto.mapper.CoursMapper;
import com.perm.dto.request.CoursRequestDTO;
import com.perm.exceptions.AccessDeniedException;
import com.perm.exceptions.ResourceNotFoundException;
import com.perm.models.AutoEcole;
import com.perm.models.cours.Cours;
import com.perm.models.cours.CoursPrive;
import com.perm.models.cours.CoursPublic;
import com.perm.models.user.Client;
import com.perm.repositories.AutoEcoleRepository;
import com.perm.repositories.CoursPriveRepository;
import com.perm.repositories.CoursPublicRepository;
import com.perm.repositories.CoursRepository;

import com.perm.services.interfaces.CoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoursServiceImpl implements CoursService {

    private final CoursRepository coursRepository;
    private final CoursPriveRepository coursPriveRepository;
    private final CoursPublicRepository coursPublicRepository;
    private final AutoEcoleRepository autoEcoleRepository;
    private final CoursMapper coursMapper;

    @Autowired
    public CoursServiceImpl(
            CoursRepository coursRepository,
            CoursPriveRepository coursPriveRepository,
            CoursPublicRepository coursPublicRepository,
            AutoEcoleRepository autoEcoleRepository,
            CoursMapper coursMapper) {
        this.coursRepository = coursRepository;
        this.coursPriveRepository = coursPriveRepository;
        this.coursPublicRepository = coursPublicRepository;
        this.autoEcoleRepository = autoEcoleRepository;
        this.coursMapper = coursMapper;
    }

    @Override
    @Transactional
    public CoursDTO createCours(CoursRequestDTO dto) {
        // Récupérer l'auto-école
        AutoEcole autoEcole = autoEcoleRepository.findById(dto.getAutoEcoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Auto-école non trouvée avec l'id : " + dto.getAutoEcoleId()));

        Cours cours;
        if ("PRIVE".equals(dto.getType())) {
            cours = coursMapper.toPriveEntity(dto, autoEcole);
        } else if ("PUBLIC".equals(dto.getType())) {
            cours = coursMapper.toPublicEntity(dto, autoEcole);
        } else {
            throw new IllegalArgumentException("Type de cours invalide. Utilisez 'PRIVE' ou 'PUBLIC'");
        }

        cours = coursRepository.save(cours);
        return coursMapper.toDTO(cours);
    }

    @Override
    @Transactional(readOnly = true)
    public CoursDTO getCours(Long id, Client client) {
        Cours cours = findCoursById(id);

        // Vérifier l'accès pour les cours privés
        if (cours instanceof CoursPrive) {
            if (client == null || !((CoursPrive) cours).verifierAcces(client)) {
                throw new AccessDeniedException("Vous n'avez pas accès à ce cours privé");
            }
        }

        // Incrémenter les vues pour les cours publics
        if (cours instanceof CoursPublic) {
            ((CoursPublic) cours).incrementerVues();
            coursRepository.save(cours);
        }

        return coursMapper.toDTO(cours);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoursDTO> getAllCours(Client client) {
        List<Cours> cours = new ArrayList<>();

        // Ajouter tous les cours publics
        cours.addAll(coursPublicRepository.findByActifTrue());

        // Ajouter les cours privés accessibles au client
        if (client != null && client.getAutoEcole() != null) {
            cours.addAll(coursPriveRepository.findActiveByAutoEcole(client.getAutoEcole().getId()));
        }

        return cours.stream()
                .map(coursMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoursDTO> getCoursByAutoEcole(Long autoEcoleId, Client client) {
        // Vérifier si le client a accès aux cours privés de cette auto-école
        boolean hasAccess = (client != null && client.getAutoEcole() != null &&
                client.getAutoEcole().getId().equals(autoEcoleId));

        List<Cours> cours = new ArrayList<>();

        // Ajouter tous les cours publics de cette auto-école
        coursPublicRepository.findAll().stream()
                .filter(c -> c.getAutoEcole().getId().equals(autoEcoleId) && c.isActif())
                .forEach(cours::add);

        // Ajouter les cours privés si le client a accès
        if (hasAccess) {
            coursPriveRepository.findActiveByAutoEcole(autoEcoleId).forEach(cours::add);
        }

        return cours.stream()
                .map(coursMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CoursDTO updateCours(Long id, CoursRequestDTO dto) {
        Cours cours = findCoursById(id);

        // Mise à jour des propriétés communes
        coursMapper.updateCoursFromDto(cours, dto);

        // Mise à jour de l'auto-école si nécessaire
        if (dto.getAutoEcoleId() != null &&
                (cours.getAutoEcole() == null || !cours.getAutoEcole().getId().equals(dto.getAutoEcoleId()))) {
            AutoEcole autoEcole = autoEcoleRepository.findById(dto.getAutoEcoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Auto-école non trouvée avec l'id : " + dto.getAutoEcoleId()));
            cours.setAutoEcole(autoEcole);
        }

        Cours updatedCours = coursRepository.save(cours);
        return coursMapper.toDTO(updatedCours);
    }

    @Override
    @Transactional
    public void deleteCours(Long id) {
        coursRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void activerCours(Long id) {
        Cours cours = findCoursById(id);
        cours.setActif(true);
        coursRepository.save(cours);
    }

    @Override
    @Transactional
    public void desactiverCours(Long id) {
        Cours cours = findCoursById(id);
        cours.setActif(false);
        coursRepository.save(cours);
    }

    @Override
    @Transactional(readOnly = true)
    public String consulterContenu(Long id, Client client) {
        Cours cours = findCoursById(id);

        // Vérifier l'accès pour les cours privés
        if (cours instanceof CoursPrive) {
            if (client == null || !((CoursPrive) cours).verifierAcces(client)) {
                throw new AccessDeniedException("Vous n'avez pas accès au contenu de ce cours privé");
            }
        }

        // Incrémenter les vues pour les cours publics
        if (cours instanceof CoursPublic) {
            ((CoursPublic) cours).incrementerVues();
            coursRepository.save(cours);
        }

        return cours.consulter();
    }

    @Override
    @Transactional
    public void modifierContenu(Long id, String nouveauContenu) {
        Cours cours = findCoursById(id);
        cours.setContenu(nouveauContenu);
        coursRepository.save(cours);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoursDTO> rechercherCours(String motCle, Client client) {
        // Trouver les cours par titre
        List<Cours> coursParTitre = coursRepository.findByTitreContainingIgnoreCase(motCle);

        // Filtrer selon l'accès
        List<Cours> coursAccessibles = coursParTitre.stream()
                .filter(cours -> {
                    if (cours instanceof CoursPublic) {
                        return true;
                    } else if (cours instanceof CoursPrive && client != null) {
                        return ((CoursPrive) cours).verifierAcces(client);
                    }
                    return false;
                })
                .collect(Collectors.toList());

        return coursAccessibles.stream()
                .map(coursMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoursDTO> getCoursActifs(Client client) {
        List<Cours> cours = new ArrayList<>(coursPublicRepository.findByActifTrue());

        if (client != null && client.getAutoEcole() != null) {
            cours.addAll(coursPriveRepository.findActiveByAutoEcole(client.getAutoEcole().getId()));
        }

        return cours.stream()
                .map(coursMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verifierAccesCours(Long coursId, Client client) {
        if (client == null) {
            return false;
        }

        Cours cours = findCoursById(coursId);

        if (cours instanceof CoursPublic) {
            return true;
        } else if (cours instanceof CoursPrive) {
            return ((CoursPrive) cours).verifierAcces(client);
        }

        return false;
    }

    // Méthode utilitaire pour trouver un cours par ID
    private Cours findCoursById(Long id) {
        return coursRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé avec l'id : " + id));
    }
}