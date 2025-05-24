package com.perm.dto.response;

import lombok.Data;

@Data
public class StatistiquesAutoEcole {

    private long nombreClients;
    private long nombreMoniteurs;
    private long nombreCours;
    private long nombreTestsBlancs;
    private double tauxReussiteExamen;
    private int nombreHeuresFormation;
    // Autres statistiques pertinentes
}