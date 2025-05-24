package com.perm.models.user;

import com.perm.models.AutoEcole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("ADMIN")
@Table(name = "admins")
public class Admin extends Utilisateur {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auto_ecole_id", nullable = false)
    private AutoEcole autoEcole;

    // Méthodes de gestion - implémentées dans les services
    public void gererClients() {
        // Implémentation dans AdminService
    }

    public Long getAutoEcoleId() {
        return this.autoEcole != null ? this.autoEcole.getId() : null;
    }

    public void gererMoniteurs() {
        // Implémentation dans AdminService
    }

    public void creerCours() {
        // Implémentation dans CoursService
    }

    public void creerTestBlanc() {
        // Implémentation dans TestBlancService
    }

    public void consulterStatistiques() {
        // Implémentation dans StatistiqueService
    }

    public void repondreMessage() {
        // Implémentation dans CommunicationService
    }
}