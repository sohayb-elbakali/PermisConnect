package com.perm.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CoursPriveDTO extends CoursDTO {
    // Champs spécifiques de CoursPrive si besoin
    private Long autoEcoleId;
}
