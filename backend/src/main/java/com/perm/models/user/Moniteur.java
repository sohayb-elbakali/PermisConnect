package com.perm.models.user;


import com.perm.models.AutoEcole;
import com.perm.models.Reservation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DiscriminatorValue("MONITEUR")
@Table(name = "moniteurs")
public class Moniteur extends Utilisateur {

    @Column
    private String specialite;

    @Column
    private Integer experience;

    @Column
    private String disponibilite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auto_ecole_id")
    private AutoEcole autoEcole;

    @OneToMany(mappedBy = "moniteur", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

    public void attribuerNote() {
        // Implémentation dans MoniteurService
    }

    public List<Reservation> consulterReservations() {
        // Implémentation dans ReservationService
        return this.reservations;
    }

    public void evaluerClient() {
        // Implémentation dans MoniteurService
    }

    public void modifierDisponibilite() {
        // Implémentation dans MoniteurService
    }
}