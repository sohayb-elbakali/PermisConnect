package com.perm.dto.mapper;

import com.perm.dto.response.AutoEcoleResponse;
import com.perm.models.AutoEcole;
import com.perm.repositories.ClientRepository;
import com.perm.repositories.CoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AutoEcoleMapper {

    private final ClientRepository clientRepository;
    private final CoursRepository coursRepository;
    //private final TestBlancRepository testBlancRepository;

    @Autowired
    public AutoEcoleMapper(
            ClientRepository clientRepository,
            CoursRepository coursRepository
            /*TestBlancRepository testBlancRepository*/) {
        this.clientRepository = clientRepository;
        this.coursRepository = coursRepository;
        //this.testBlancRepository = testBlancRepository;
    }

    public AutoEcoleResponse toAutoEcoleResponse(AutoEcole autoEcole) {
        AutoEcoleResponse response = new AutoEcoleResponse();
        response.setId(autoEcole.getId());
        response.setNom(autoEcole.getNom());
        response.setAdresse(autoEcole.getAdresse());
        response.setTelephone(autoEcole.getTelephone());
        response.setEmail(autoEcole.getEmail());
        response.setNumeroAgrement(autoEcole.getNumeroAgrement());
        response.setDateCreation(autoEcole.getDateCreation());

        // Informations sur l'admin
        if (autoEcole.getAdmin() != null) {
            response.setAdminId(autoEcole.getAdmin().getId());
            response.setAdminNom(autoEcole.getAdmin().getNom() + " " + autoEcole.getAdmin().getPrenom());
        }

        // Liste des noms des moniteurs
        response.setMoniteurs(autoEcole.getMoniteurs().stream()
                .map(m -> m.getNom() + " " + m.getPrenom())
                .collect(Collectors.toList()));

        // Statistiques
        response.setNombreClients(autoEcole.getClients().size());
        response.setNombreCours(autoEcole.getCours().size());
        response.setNombreTestsBlancs(autoEcole.getTestsBlancs().size());

        return response;
    }
}