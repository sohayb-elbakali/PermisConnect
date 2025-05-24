package com.perm.models;


import com.perm.models.base.AuditableEntity;
import com.perm.models.user.Client;
import com.perm.models.user.Moniteur;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "reservations")
public class Reservation extends AuditableEntity {

    @Column(name = "date_heure_debut", nullable = false)
    private LocalDateTime dateHeureDebut;

    @Column(name = "date_heure_fin", nullable = false)
    private LocalDateTime dateHeureFin;

    @Column(nullable = false)
    private String statut; // PENDING, CONFIRMED, CANCELED, COMPLETED

    @Column(name = "type_seance")
    private String typeSeance; // CONDUITE, THÉORIE, etc.

    @Column(name = "note_conduite")
    private Double noteConduite;

    @Column
    private String commentaire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moniteur_id")
    private Moniteur moniteur;

    public void confirmer() {
        this.statut = "CONFIRMED";
    }

    public void annuler() {
        this.statut = "CANCELED";
    }

    public void terminer() {
        this.statut = "COMPLETED";
    }

    public void attribuerNote(Double note) {
        this.noteConduite = note;
    }
}