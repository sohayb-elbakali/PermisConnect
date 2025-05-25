package com.perm.models.user;


import com.perm.models.AutoEcole;
import com.perm.models.Diagnostic;
import com.perm.models.Reservation;
import com.perm.models.communication.Communication;
import com.perm.models.cours.Cours;
import com.perm.models.test.ResultatTest;
import com.perm.utils.SwaggerIgnoreCircularDependencies;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DiscriminatorValue("CLIENT")
@Table(name = "clients")
public class Client extends Utilisateur {

    @Column
    private Double progression;

    @Column(name = "date_inscription")
    private LocalDateTime dateInscription;

    @Column
    private String statut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auto_ecole_id")
    @SwaggerIgnoreCircularDependencies
    private AutoEcole autoEcole;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(hidden = true)
    @SwaggerIgnoreCircularDependencies
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(hidden = true)
    @SwaggerIgnoreCircularDependencies
    private List<ResultatTest> resultatsTests = new ArrayList<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(hidden = true)
    @SwaggerIgnoreCircularDependencies
    private List<Communication> communications = new ArrayList<>();

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(hidden = true)
    @SwaggerIgnoreCircularDependencies
    private Diagnostic diagnostic;

    public Diagnostic consulterDiagnostic() {
        // Implémentation dans DiagnosticService
        return this.diagnostic;
    }

    public void reserverCours() {
        // Implémentation dans ReservationService
    }

    public void passerTestBlanc() {
        // Implémentation dans TestBlancService
    }

    public void envoyerMessage() {
        // Implémentation dans CommunicationService
    }

    public List<Cours> consulterCours() {
        // Implémentation dans CoursService
        return new ArrayList<>();
    }
}
