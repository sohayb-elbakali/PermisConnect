package com.perm.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AutoEcoleResponse {

    private Long id;
    private String nom;
    private String adresse;
    private String telephone;
    private String email;
    private String numeroAgrement;
    private LocalDateTime dateCreation;
    private Long adminId;
    private String adminNom;
    private List<String> moniteurs; // Noms des moniteurs
    private long nombreClients;
    private long nombreCours;
    private long nombreTestsBlancs;
}