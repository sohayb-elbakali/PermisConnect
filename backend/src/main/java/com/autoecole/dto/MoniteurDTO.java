package com.autoecole.dto;

import lombok.Data;

@Data
public class MoniteurDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    // Add more fields if needed, but DO NOT include password or recursive objects!
} 