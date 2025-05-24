package com.perm.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CoursRequestDTO {
    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 3, max = 100, message = "Le titre doit contenir entre 3 et 100 caractères")
    private String titre;

    private String description;

    private String contenu;

    private Integer duree;

    private Long autoEcoleId;

    private boolean actif = true;

    private String type; // "PRIVE" ou "PUBLIC"
}
