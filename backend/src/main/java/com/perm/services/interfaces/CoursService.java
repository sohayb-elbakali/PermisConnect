package com.perm.services.interfaces;


import com.perm.dto.CoursDTO;
import com.perm.dto.request.CoursRequestDTO;
import com.perm.models.user.Client;

import java.util.List;

public interface CoursService {

    CoursDTO createCours(CoursRequestDTO coursDTO);

    CoursDTO getCours(Long id, Client client);

    List<CoursDTO> getAllCours(Client client);

    List<CoursDTO> getCoursByAutoEcole(Long autoEcoleId, Client client);

    CoursDTO updateCours(Long id, CoursRequestDTO coursDTO);

    void deleteCours(Long id);

    void activerCours(Long id);

    void desactiverCours(Long id);

    String consulterContenu(Long id, Client client);

    void modifierContenu(Long id, String nouveauContenu);

    List<CoursDTO> rechercherCours(String motCle, Client client);

    List<CoursDTO> getCoursActifs(Client client);

    boolean verifierAccesCours(Long coursId, Client client);
}
