package com.perm.dto.mapper;

import com.perm.dto.CoursDTO;
import com.perm.dto.CoursPriveDTO;
import com.perm.dto.CoursPublicDTO;
import com.perm.dto.request.CoursRequestDTO;
import com.perm.models.AutoEcole;
import com.perm.models.cours.Cours;
import com.perm.models.cours.CoursPrive;
import com.perm.models.cours.CoursPublic;
import org.springframework.stereotype.Component;

@Component
public class CoursMapper {

    public CoursDTO toDTO(Cours cours) {
        if (cours == null) {
            return null;
        }

        CoursDTO dto;
        if (cours instanceof CoursPrive) {
            CoursPriveDTO coursPriveDTO = new CoursPriveDTO();
            coursPriveDTO.setAutoEcoleId(((CoursPrive) cours).getAutoEcole().getId());
            dto = coursPriveDTO;
            dto.setType("PRIVE");
        } else if (cours instanceof CoursPublic) {
            CoursPublicDTO coursPublicDTO = new CoursPublicDTO();
            coursPublicDTO.setNombreVues(((CoursPublic) cours).getNombreVues());
            dto = coursPublicDTO;
            dto.setType("PUBLIC");
        } else {
            dto = new CoursDTO();
        }

        dto.setId(cours.getId());
        dto.setTitre(cours.getTitre());
        dto.setDescription(cours.getDescription());
        dto.setDuree(cours.getDuree());
        dto.setActif(cours.isActif());

        if (cours.getAutoEcole() != null) {
            dto.setAutoEcoleId(cours.getAutoEcole().getId());
            dto.setAutoEcoleNom(cours.getAutoEcole().getNom());
        }

        dto.setDateCreation(cours.getDateCreation());
        dto.setDateModification(cours.getDateModification());
        //dto.setCreePar(cours.getCreePar());
        //dto.setModifiePar(cours.getModifiePar());

        return dto;
    }

    public CoursPrive toPriveEntity(CoursRequestDTO dto, AutoEcole autoEcole) {
        CoursPrive cours = new CoursPrive();
        updateCoursFromDto(cours, dto);
        cours.setAutoEcole(autoEcole);
        return cours;
    }

    public CoursPublic toPublicEntity(CoursRequestDTO dto, AutoEcole autoEcole) {
        CoursPublic cours = new CoursPublic();
        updateCoursFromDto(cours, dto);
        cours.setAutoEcole(autoEcole);
        cours.setNombreVues(0);
        return cours;
    }

    public void updateCoursFromDto(Cours cours, CoursRequestDTO dto) {
        cours.setTitre(dto.getTitre());
        cours.setDescription(dto.getDescription());
        cours.setContenu(dto.getContenu());
        cours.setDuree(dto.getDuree());
        cours.setActif(dto.isActif());
    }
}