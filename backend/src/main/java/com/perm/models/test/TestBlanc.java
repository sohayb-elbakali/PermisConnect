package com.perm.models.test;


import com.perm.models.AutoEcole;
import com.perm.models.base.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tests_blancs")
public class TestBlanc extends AuditableEntity {





    @Column(nullable = false)
    private String titre;

    @Column
    private String description;

    @Column(name = "duree_minutes")
    private Integer dureeMinutes;

    @Column(name = "note_passage")
    private Double notePassage;

    @Column
    private boolean actif = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auto_ecole_id")
    private AutoEcole autoEcole;

    @OneToMany(mappedBy = "testBlanc", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "testBlanc", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResultatTest> resultats = new ArrayList<>();

    public List<Question> genererQuestions() {
        // Implémentation dans TestBlancService
        return this.questions;
    }

    public Double calculerNote() {
        // Implémentation dans TestBlancService
        return 0.0;
    }

    public ResultatTest obtenirResultat() {
        // Implémentation dans TestBlancService
        return null;
    }
}