package com.perm.models;


import com.perm.models.base.AuditableEntity;
import com.perm.models.user.Client;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "diagnostics")
public class Diagnostic extends AuditableEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "progression_globale")
    private Double progressionGlobale;

    @Column(name = "moyenne_tests_blancs")
    private Double moyenneTestsBlancs;

    @Column(name = "moyenne_conduite")
    private Double moyenneConduite;

    @Column(name = "nombre_heures_effectuees")
    private Integer nombreHeuresEffectuees;

    @Column(name = "date_generee")
    private LocalDateTime dateGeneree;

    @ElementCollection
    @CollectionTable(name = "recommandations", joinColumns = @JoinColumn(name = "diagnostic_id"))
    @Column(name = "recommandation")
    private List<String> recommandations = new ArrayList<>();

    public Double calculerProgression() {
        // Implémentation dans DiagnosticService
        return this.progressionGlobale;
    }

    public String genererRapport() {
        // Implémentation dans DiagnosticService
        return "Rapport pour " + client.getNom() + " " + client.getPrenom() +
                ": Progression globale: " + progressionGlobale + "%";
    }

    public List<String> obtenirRecommandations() {
        // Implémentation dans DiagnosticService
        return this.recommandations;
    }
}
