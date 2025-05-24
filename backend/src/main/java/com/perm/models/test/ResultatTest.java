package com.perm.models.test;

import com.perm.models.base.AuditableEntity;
import com.perm.models.user.Client;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "resultats_tests")
public class ResultatTest extends AuditableEntity {

    @Column(nullable = false)
    private Double note;

    @Column(name = "temps_ecoule")
    private Integer tempsEcoule;

    @Column(name = "date_passage")
    private LocalDateTime datePassage;

    @Column
    private boolean reussi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_blanc_id")
    private TestBlanc testBlanc;

    public Double calculerPourcentage() {
        // Calcul simple du pourcentage de réussite
        if (testBlanc != null && testBlanc.getNotePassage() != null) {
            return (note / testBlanc.getNotePassage()) * 100;
        }
        return 0.0;
    }
}
